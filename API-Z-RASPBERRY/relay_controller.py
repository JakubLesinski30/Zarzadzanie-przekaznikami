# relay_controller.py

import time

try:
    from IOPi import IOPi
except ImportError:
    raise ImportError("Failed to import IOPi library. Upewnij się, że jest zainstalowana biblioteka IOPi.")

class RelayController:
    """
    Klasa zarządzająca wieloma ekspanderami MCP23017 (np. IO Pi Plus).
    Każdy ekspander (płytka) ma 16 pinów (przekaźników).
    """

    # Wszystkie możliwe adresy MCP23017 (8 sztuk: 0x20 do 0x27)
    ALL_ADDRESSES = [0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27]

    def __init__(self):
        """
        Zawsze inicjalizujemy wszystkie adresy 0x20..0x27.
        Jeśli na którymś adresie brak płytki, ignorujemy błąd.
        """
        self.addresses = self.ALL_ADDRESSES
        self.boards = []

        for addr in self.addresses:
            try:
                bus = IOPi(addr)
                # Ustawienie wszystkich portów na wyjście
                bus.set_port_direction(0, 0x00)  # Port A (piny 1-8)
                bus.set_port_direction(1, 0x00)  # Port B (piny 9-16)
                # Na starcie wszystkie przekaźniki OFF
                bus.write_bus(0xFFFF)

                self.boards.append(bus)
                print(f"Zainicjalizowano płytkę na adresie {hex(addr)}")
            except OSError as e:
                # Np. "Input/output error" - brak fizycznie takiej płytki
                print(f"UWAGA: Nie można zainicjalizować płytki na adresie {hex(addr)}. Błąd: {e}")
                # Nie przerywamy działania – po prostu pomijamy tę płytkę.

    def set_relay(self, board_index, relay_pin, state):
        """
        Ustawia stan pojedynczego przekaźnika na wybranej płytce.

        :param board_index: indeks płytki w liście self.boards (0..N-1)
        :param relay_pin: pin przekaźnika w zakresie 1..16
        :param state: 1 (ON) lub 0 (OFF)
        """

        if not (0 <= board_index < len(self.boards)):
            raise IndexError(
                f"Niepoprawny indeks płytki: {board_index}. "
                f"Masz dostępne płytki w zakresie: 0..{len(self.boards)-1}"
            )

        if not (1 <= relay_pin <= 16):
            raise ValueError(
                f"Numer przekaźnika powinien być w zakresie 1..16, podano {relay_pin}."
            )

        # Jeżeli Twoje przekaźniki działają w logice "active low"
        # i chcesz mieć 1=ON, 0=OFF – odwróć stan:
        inverted_state = 0 if state == 1 else 1

        self.boards[board_index].write_pin(relay_pin, inverted_state)

    def turn_all_on(self):
        """
        Włącza wszystkie przekaźniki na wszystkich wykrytych płytkach.
        (dla układów active low -> 0 na pinie = ON)
        """
        for bus in self.boards:
            bus.write_bus(0x0000)  # 0x0000 = wszystkie piny 0

    def turn_all_off(self):
        """
        Wyłącza wszystkie przekaźniki na wszystkich wykrytych płytkach.
        (dla układów active low -> 1 na pinie = OFF)
        """
        for bus in self.boards:
            bus.write_bus(0xFFFF)  # 0xFFFF = wszystkie piny 1

    def demo_sequence(self, delay=0.5):
        """
        Prosta sekwencja testowa: włącza i wyłącza po kolei przekaźniki
        na każdej dostępnej płytce (która została pomyślnie zainicjalizowana).
        """
        print("Rozpoczynam sekwencję DEMO...")
        try:
            for i, bus in enumerate(self.boards):
                print(f"\n--- Płytka nr {i} (adres: {hex(self.addresses[i])}) ---")

                # Włączanie po kolei
                for pin in range(1, 17):
                    print(f"Włączam przekaźnik {pin} na płytce {i}")
                    bus.write_pin(pin, 0)  # 0 => ON przy active low
                    time.sleep(delay)

                # Wyłączanie po kolei
                for pin in range(1, 17):
                    print(f"Wyłączam przekaźnik {pin} na płytce {i}")
                    bus.write_pin(pin, 1)  # 1 => OFF przy active low
                    time.sleep(delay)

            print("\nZakończono sekwencję DEMO.")
        except KeyboardInterrupt:
            print("Przerwano sekwencję. Wyłączam wszystkie przekaźniki...")
            self.turn_all_off()

    def get_relay_state(self, board_index, relay_pin):
        """
        Zwraca logiczny stan przekaźnika: 1 = ON, 0 = OFF
        przy założeniu, że fizycznie jest active-low.
        """
        # Walidacja indeksu płytki:
        if not (0 <= board_index < len(self.boards)):
            raise IndexError(f"Niepoprawny indeks płytki: {board_index}. "
                         f"Masz dostępne płytki w zakresie: 0..{len(self.boards)-1}")

        # Walidacja numeru pinu:
        if not (1 <= relay_pin <= 16):
            raise ValueError(f"Numer przekaźnika powinien być w zakresie 1..16, podano {relay_pin}.")

        # Odczyt fizycznego stanu pinu (1 = stan wysoki, 0 = stan niski).
        physical_state = self.boards[board_index].read_pin(relay_pin)

        # Jeżeli przekaźnik jest "active low", to fizyczny stan 0 oznacza włączony (ON),
        # a fizyczny stan 1 oznacza wyłączony (OFF).
        # Dlatego jeśli chcemy w API zwrócić 1=ON, 0=OFF, musimy odwrócić tę wartość:
        if physical_state == 0:
            return 1  # ON
        else:
            return 0  # OFF
        
    def get_all_relays_in_board(self, board_index):
        """
        Zwraca listę 16 elementów (dla pinów 1..16).
        Każdy element to 1=ON lub 0=OFF (przy active low).
        """
        if not (0 <= board_index < len(self.boards)):
            raise IndexError(f"Niepoprawny indeks płytki: {board_index}")

        # read_bus() zwraca liczbę 16-bit (0..65535),
        # gdzie bit 0 odpowiada pinowi 1, bit 1 -> pin 2, itd.
        # (zależnie od biblioteki IOPi).
        bus_state = self.boards[board_index].read_bus()

        # Każdy bit w bus_state to fizyczny stan pinu (1=wysoki, 0=niski).
        # Dla active low: 0=ON, 1=OFF
        # Poniżej "rozbijamy" to na 16 osobnych bitów i robimy inwersję
        states = []
        for pin in range(16):
            bit_value = (bus_state >> pin) & 0x1
            # bit_value = 0 => ON (logicznie 1), 1 => OFF (logicznie 0)
            states.append(1 if bit_value == 0 else 0)

        # states[0] to stan pinu 1, states[1] to stan pinu 2, itd.
        return states

    def get_all_boards_state(self):
        """
        Zwraca listę słowników opisujących stany wszystkich pinów
        na wszystkich dostępnych płytkach.

        Przykład zwracanej listy:
        [
        {
            "board_index": 0,
            "address": "0x20",
            "pins": [1, 0, 0, 1, ..., 16 wartości]
        },
        {
            "board_index": 1,
            "address": "0x21",
            "pins": [0, 1, 1, 1, ..., 16 wartości]
        },
        ...
        ]
        """
        result = []
        for i, bus in enumerate(self.boards):
            # Odczytuję adres I2C (podobnie jak w self.addresses[i])
            addr_hex = hex(self.addresses[i]) if i < len(self.addresses) else f"{i}"

            # Stan 16-bitowy (0..65535), bity 0..15
            bus_state = bus.read_bus()

            # Rozbicie na 16 pinów, z jednoczesną inwersją (active low)
            # Fizycznie 0=ON -> logicznie 1, 1=OFF -> logicznie 0
            pins_state = []
            for pin_bit in range(16):
                bit_val = (bus_state >> pin_bit) & 0x1
                # bit_val=0 => ON => 1, bit_val=1 => OFF => 0
                pins_state.append(1 if bit_val == 0 else 0)

            # pins_state[0] odpowiada pinowi nr 1, pins_state[1] = pin nr 2, itd.
            # Można chcieć odwrócić kolejność, ale to już kwestia preferencji.
            result.append({
                "board_index": i,
                "address": addr_hex,
                "pins": pins_state
            })

        return result
    
    def set_relay_by_number(self, relay_number, state):
        """
        Ustawia stan przekaźnika, korzystając z numeru 1..128.
        :param relay_number: int, 1..128
        :param state: 1 (ON) lub 0 (OFF)
        """
        if relay_number < 1 or relay_number > 128:
            raise ValueError(f"relay_number musi być w zakresie 1..128, otrzymano {relay_number}")

        # Obliczamy, która płytka (board_index) i który pin (1..16)
        board_index = (relay_number - 1) // 16
        pin = ((relay_number - 1) % 16) + 1

        # Wywołujemy istniejącą metodę set_relay()
        self.set_relay(board_index, pin, state)

    def get_relay_state_by_number(self, relay_number):
        """
        Zwraca aktualny stan przekaźnika (1=ON, 0=OFF) wg numeru 1..128.
        """
        if relay_number < 1 or relay_number > 128:
            raise ValueError(f"relay_number musi być w zakresie 1..128, otrzymano {relay_number}")

        board_index = (relay_number - 1) // 16
        pin = ((relay_number - 1) % 16) + 1

        return self.get_relay_state(board_index, pin)

    def get_all_numbered_relays_state(self):
        """
        Zwraca listę słowników { "relay_number": X, "state": Y }
        dla wszystkich fizycznie podłączonych przekaźników.
        """
        states = []
        total_relays = len(self.boards) * 16  # tyle mamy łącznie przekaźników

        for relay_num in range(1, total_relays + 1):
            st = self.get_relay_state_by_number(relay_num)
            # st = 1 (ON) lub 0 (OFF) w logice "normalnej" (active-low odwrócone)
            states.append({
                "relay_number": relay_num,
                "state": st
            })

        return states