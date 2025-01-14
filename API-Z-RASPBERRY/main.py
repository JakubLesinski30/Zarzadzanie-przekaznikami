# main.py
from flask import Flask, request, jsonify
from relay_controller import RelayController
from temperature_reader import TemperatureReader
from flask import Flask, render_template

# Tworzymy kontroler - zawsze spróbuje wykryć płytki 0x20..0x27
controller = RelayController()

temperature_reader = TemperatureReader(interval=2)

app = Flask(__name__)

@app.route("/relay/set", methods=["POST"])
def set_relay():
    # POBIERANIE PARAMETRÓW Z QUERY STRING
    board_index = request.args.get("board_index", default=0, type=int)
    relay_pin = request.args.get("relay_pin", default=1, type=int)
    state = request.args.get("state", default=0, type=int)

    try:
        controller.set_relay(board_index, relay_pin, state)
        return jsonify({
            "status": "OK",
            "message": f"Ustawiono pin {relay_pin} na płytce {board_index} w stan {state}"
        }), 200
    except Exception as e:
        return jsonify({"status": "ERROR", "message": str(e)}), 400

@app.route("/relay/on_all", methods=["POST"])
def on_all():
    controller.turn_all_on()
    return jsonify({"status": "OK", "message": "Wszystkie przekaźniki włączone"}), 200

@app.route("/relay/off_all", methods=["POST"])
def off_all():
    controller.turn_all_off()
    return jsonify({"status": "OK", "message": "Wszystkie przekaźniki wyłączone"}), 200

@app.route("/demo", methods=["GET"])
def demo():
    controller.demo_sequence(delay=0.5)
    return jsonify({"status": "OK", "message": "Demo sequence finished"}), 200

@app.route("/relay/state", methods=["GET"])
def get_relay():
    """
    Zwraca aktualny stan przekaźnika (ON=1, OFF=0).
    Przykład wywołania:
      GET /relay/state?board_index=0&relay_pin=1
    """
    board_index = request.args.get("board_index", default=0, type=int)
    relay_pin = request.args.get("relay_pin", default=1, type=int)

    try:
        state = controller.get_relay_state(board_index, relay_pin)
        return jsonify({
            "status": "OK",
            "board_index": board_index,
            "relay_pin": relay_pin,
            "state": state
        }), 200
    except Exception as e:
        return jsonify({"status": "ERROR", "message": str(e)}), 400
    
@app.route("/relay/state_all", methods=["GET"])
def get_all_relays():
    board_index = request.args.get("board_index", default=0, type=int)
    try:
        states = controller.get_all_relays_in_board(board_index)
        return jsonify({
            "status": "OK",
            "board_index": board_index,
            "relays": states  # np. tablica 16 wartości: [1, 0, 1, 1, ...]
        }), 200
    except Exception as e:
        return jsonify({"status": "ERROR", "message": str(e)}), 400
    
@app.route("/relay/relay_state_all", methods=["GET"])
def get_all_boards_state():
    """
    Zwraca stany wszystkich pinów na wszystkich płytkach w jednym obiekcie JSON.
    """
    try:
        # Pobieramy listę słowników z poprzedniej metody
        boards_state = controller.get_all_boards_state()
        return jsonify({
            "status": "OK",
            "message": "All boards state read successfully",
            "data": boards_state
        }), 200
    except Exception as e:
        return jsonify({"status": "ERROR", "message": str(e)}), 400
    
@app.route("/relay/set_number", methods=["POST"])
def set_relay_number():
    """
    Ustawia stan przekaźnika na podstawie relay_number (1..128).
    Parametry w query string:
      - relay_number (int)
      - state (int): 0 lub 1
    """
    relay_number = request.args.get("relay_number", default=1, type=int)
    state = request.args.get("state", default=0, type=int)

    try:
        controller.set_relay_by_number(relay_number, state)
        return jsonify({
            "status": "OK",
            "message": f"Ustawiono przekaźnik nr {relay_number} w stan {state}"
        }), 200
    except Exception as e:
        return jsonify({"status": "ERROR", "message": str(e)}), 400
    
@app.route("/relay/state_number", methods=["GET"])
def get_relay_number():
    """
    Zwraca aktualny stan przekaźnika (1=ON, 0=OFF) wg relay_number (1..128).
    Parametry:
      - relay_number (int)
    """
    relay_number = request.args.get("relay_number", default=1, type=int)

    try:
        state = controller.get_relay_state_by_number(relay_number)
        return jsonify({
            "status": "OK",
            "relay_number": relay_number,
            "state": state
        }), 200
    except Exception as e:
        return jsonify({"status": "ERROR", "message": str(e)}), 400
    
@app.route("/relay/number_state_all", methods=["GET"])
def get_all_relays_numbered():
    """
    Zwraca stany wszystkich dostępnych przekaźników (wg numeracji 1..N).
    """
    try:
        data = controller.get_all_numbered_relays_state()
        return jsonify({
            "status": "OK",
            "relays": data
        }), 200
    except Exception as e:
        return jsonify({"status": "ERROR", "message": str(e)}), 400
    
@app.route("/temperature", methods=["GET"])
def get_temperature():
    """
    Zwraca słownik temperatur z wykrytych czujników DS18B20.
    Format:
    {
      "status": "OK",
      "sensors": [
        {"id": "28-xxxx", "temperature": 21.5},
        {"id": "28-yyyy", "temperature": 23.1}
      ]
    }
    """
    data = temperature_reader.get_temperatures()

    # Zamieniamy dict -> listę obiektów bardziej czytelnych w JSON
    sensors = []
    for sensor_id, temp_c in data.items():
        sensors.append({
            "id": sensor_id,
            "temperature": temp_c
        })

    return jsonify({
        "status": "OK",
        "sensors": sensors
    }), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
