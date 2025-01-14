# temperature_reader.py

import os
import glob
import time
import threading

class TemperatureReader:
    """
    Klasa cyklicznie odświeżająca odczyt z czujników DS18B20
    (podłączonych do GPIO21, co wymaga dtoverlay=w1-gpio,gpiopin=21
    w pliku /boot/config.txt).
    """
    def __init__(self, interval=2):
        """
        :param interval: co ile sekund odświeżać odczyt z czujników
        """
        self.interval = interval
        # Słownik: { "28-xxxx": ostatni_odczyt_temperatury_w_stopniach_C }
        self.sensor_data = {}

        # Startujemy wątek w trybie daemon, by aplikacja mogła się zamknąć
        self.thread = threading.Thread(target=self._update_loop, daemon=True)
        self.thread.start()

    def _update_loop(self):
        while True:
            self._read_sensors()
            time.sleep(self.interval)

    def _read_sensors(self):
        """
        Przegląda katalog /sys/bus/w1/devices w poszukiwaniu czujników (28-xxxx).
        Odczytuje plik w1_slave i wyciąga z niego temperaturę.
        """
        base_dir = "/sys/bus/w1/devices/"
        # Szukamy folderów rozpoczynających się od "28-"
        for sensor_dir in glob.glob(os.path.join(base_dir, "28-*")):
            sensor_id = os.path.basename(sensor_dir)  # np. "28-0123456789ab"
            w1_file = os.path.join(sensor_dir, "w1_slave")

            if os.path.exists(w1_file):
                with open(w1_file, "r") as f:
                    lines = f.readlines()
                    # Przykładowa zawartość:
                    # e7 00 4b 46 7f ff 0c 10 26 : crc=26 YES
                    # e7 00 4b 46 7f ff 0c 10 26 t=14937
                    if "YES" in lines[0]:  # Poprawny odczyt CRC
                        temp_line = lines[1].split("t=")
                        if len(temp_line) == 2:
                            raw_temp = temp_line[1].strip()  # np. "14937"
                            temp_c = float(raw_temp) / 1000.0
                            self.sensor_data[sensor_id] = temp_c
                        else:
                            # Błędny format?
                            self.sensor_data[sensor_id] = None
                    else:
                        # CRC niepoprawne
                        self.sensor_data[sensor_id] = None

    def get_temperatures(self):
        """
        Zwraca aktualny słownik { sensor_id: temp_C }.
        Jeśli odczyt się nie udał, może być wartość None.
        """
        return dict(self.sensor_data)
