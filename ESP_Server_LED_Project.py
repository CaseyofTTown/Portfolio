from machine import Pin, SPI
from max7219 import Matrix8x8
import neopixel, network, time, socket, _thread, json, uhashlib

# --- Hardware Setup ---
# Initialize SPI communication for the LED matrix
spi = SPI(
    1,                      # SPI bus ID
    baudrate=10_000_000,    # Speed of data transfer
    polarity=0,             # Clock idle low
    phase=0,                # Data captured on first clock edge
    sck=Pin(18),            # Serial Clock pin
    mosi=Pin(11)            # Master Out, Slave In pin
)

# Initialize the LED matrix display
display = Matrix8x8(
    spi,        # SPI object
    Pin(5),     # Chip Select (CS) pin
    4           # Number of cascaded matrices
)

# Set display brightness (range: 0 to 15)
display.brightness(5)

#built in led to board
LED_PIN = 48 
NUM_LEDS = 1
np = neopixel.NeoPixel(Pin(LED_PIN), NUM_LEDS)

# RGB color tuples representing common colors - found online
colors = [
    (255,   0,   0),   # Red
    (0,   255,   0),   # Green
    (0,     0, 255),   # Blue
    (255, 255,   0),   # Yellow
    (0,   255, 255),   # Cyan
    (255,   0, 255),   # Magenta
    (0,     0,   0)    # Black
]


# --- Globals ---
#control variable for visual led indication
connection_active = False
#default message
current_message = "Testing"
#how much to shift the cypher - is in sister program as well
CAESAR_SHIFT = 6

# --- LED Loop ---
def led_loop():
    while True:
        #steady flash blue
        if connection_active:
            for b in range(0, 256, 8):
                np[0] = (0, 0, b)
                np.write()
                time.sleep(0.03)
            for b in range(255, -1, -8):
                np[0] = (0, 0, b)
                np.write()
                time.sleep(0.03)
        else:
            #cycle through all colors as defined 
            for color in colors:
                np[0] = color
                np.write()
                time.sleep(0.5)

# --- Caesar Cipher ---
def encrypt_caesar(text, shift=CAESAR_SHIFT):
    result = ""
    for c in text:
        if c.isalpha():
            base = ord("A") if c.isupper() else ord("a")
            result += chr((ord(c) - base + shift) % 26 + base)
        else:
            result += c
    return result

def decrypt_caesar(text, shift=CAESAR_SHIFT):
    result = ""
    for c in text:
        if c.isalpha():
            base = ord("A") if c.isupper() else ord("a")
            result += chr((ord(c) - base - shift) % 26 + base)
        else:
            result += c
    return result

# --- Password Handling ---
def hash_password(pw):
    return uhashlib.sha256(pw.encode()).digest().hex() #micro-py does not support hexidigest()
#check for existing users
def load_users():
    try:
        with open("users.json") as f:
            return json.load(f)
    except:
        return {}
#will create the json if not exist 
def save_users(users):
    with open("users.json", "w") as f:
        json.dump(users, f)
#new user
def register_user(username, password):
    users = load_users()
    if username in users:
        return False
    users[username] = hash_password(password)
    save_users(users)
    return True
#returning user
def login_user(username, password):
    users = load_users()
    hashed = hash_password(password) #compare against stored password which is hashed as well
    return users.get(username) == hashed #true if username exists and password matches

# --- Wi-Fi Connection ---
# connect to home network to host the socket 
def connect_wifi(ssid, password):
    wlan = network.WLAN(network.STA_IF) # Tells microcontroller to host station interface (connect to existing)
    wlan.active(True) # Activate the interface
    if not wlan.isconnected():
        print("Connecting to network...")
        wlan.connect(ssid, password)
        timeout = 15
        while not wlan.isconnected() and timeout > 0:
            time.sleep(0.5)  # Wait before attempting to reconnect
            timeout -= 0.5  # Give a shorter timeout this time
    # Print the ip to the console, or could obtain through network app, or could have set a static one 
    if wlan.isconnected():
        print("Connected. IP:", wlan.ifconfig()[0]) 
        return True 
    else:
        print("Wi-Fi failed.")
        return False


def scroll_text(text, delay=0.1): #if you wanted a single scroll
    for offset in range(len(text) * 8):  # 8 pixels per character
        display.fill(0)
        display.text(text, -offset, 0, 1)  # negative x offset scrolls left
        display.show()
        time.sleep(delay)
        
def continuous_scroll(): #non stop scrolling, start in a new thread 
    previous_message = ""
    while True:
        if current_message != previous_message:
            previous_message = current_message
        width = len(current_message) * 8
        for offset in range(width):
            display.fill(0)
            display.text(current_message, -offset, 0, 1)
            display.show()
            time.sleep(0.05)  # adjust scroll speed here



def start_socket_server():
    global connection_active, current_message
    
    # Create a tcp socket with IPv 4
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Allows immediate re-use of the socket to fix an error encountered during dubugging 
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    # Listen on all interfaces on port 5001
    s.bind(('0.0.0.0', 5001))
    # Listen for a single connection only 
    s.listen(1)
    print("Socket server listening on port 5001...") #this prints red? 0_o

    while True:
        conn, addr = s.accept()
        connection_active = True
        print("Client connected from", addr)

        try:
            # User Registration/Login starting with prompt for register/login flow
            # ALL incomming messages must be decrypted 
            conn.send(encrypt_caesar("New user? (Y/N):").encode())
            new_user = decrypt_caesar(conn.recv(8).decode().strip().upper())

            conn.send(encrypt_caesar("Username:").encode())
            username = decrypt_caesar(conn.recv(64).decode().strip())

            conn.send(encrypt_caesar("Password:").encode())
            password = decrypt_caesar(conn.recv(64).decode().strip())
            # --- Registration Flow ---
            if new_user == 'Y':
                success = register_user(username, password)
                if not success:
                    print("Registration was not successful")
                    conn.send(encrypt_caesar("Username already exists.").encode())
                    conn.close()
                    connection_active = False
                    continue
                conn.send(encrypt_caesar("Registered successfully.").encode())
            else:
                # --- Login Flow ---
                if not login_user(username, password):
                    conn.send(encrypt_caesar("Invalid credentials. Goodbye").encode())
                    conn.close()
                    connection_active = False
                    continue
                conn.send(encrypt_caesar("Login successful.").encode())

            # Main message loop for updating the LED matrix display
            while True:
                # Inform user of current message
                conn.send(encrypt_caesar(f"Current message: {current_message}\n").encode())
                # The sister program is looking for this message below in order to know when to append
                # 'MSG:' to the start of its message for security purposes. This way if someones account
                # information was comprimised, and an attacker had the IP address, they hopefully would be
                # unaware that the sister program appends this TAG automatically based on this detection and
                # would therefore be kicked from the server without affecting the message
                conn.send(encrypt_caesar("Enter a new message to display:").encode())
                raw = conn.recv(256).decode().strip()
                decrypted = decrypt_caesar(raw)
                print(decrypted)
                # Only a message prefixed with this tag should be able to modify the display
                if decrypted.startswith("MSG:"):
                    new_text = decrypted[4:]
                    current_message = new_text
                    display.fill(0)
                    #scroll_text(current_message) called multiple times results in flickering on display
                    #display.show() handled in continuous scroll function now, left as warning
                    conn.send(encrypt_caesar("Message updated.").encode())
                else:
                    print("Error in message transaction")
                    conn.send(encrypt_caesar("Invalid format or cipher. Must start with expected tag").encode())
                    conn.send(encrypt_caesar("Goodbye.").encode())
                    conn.close() # kick them out 
                    connection_active = False
                    break  # Exit the message loop
                
                conn.send(encrypt_caesar("Change again? (Y/N):").encode())
                response = decrypt_caesar(conn.recv(8).decode().strip().upper())
                # End session if user declines
                if response != 'Y':
                    conn.send(encrypt_caesar("Goodbye.").encode())
                    conn.close()
                    connection_active = False
                    break 
        # Catch any errors and print them to console
        # Can be read through WebREPL if embedded system isnt attached to data cable anymore w/ some setup
        except Exception as e:
            print("Error in session:", e)
            try:
                conn.send(encrypt_caesar("Connection error.").encode())
            except:
                pass
            conn.close()
            connection_active = False


# --- Main Entry ---
def loop():
    if connect_wifi("Your_SSID", "Your_Wifi_Password"): # would ob never do this in production 
        time.sleep(1) # Gives the network a moment to establish
        _thread.start_new_thread(led_loop, ()) # Built in led loop / connection indicator
        _thread.start_new_thread(start_socket_server, ()) # Socket server on its own thread
        _thread.start_new_thread(continuous_scroll, ()) # The matrix panels message loop 


loop()

# --- END of SERVER PRORGRAM --- BELOW IS THE CLIENT SOFTWARE --- BE SURE TO SEPARATE THESE FILES SERVER GOES ON ESP32, CLIENT RUNS ON THE PC

import socket

# Caesar Cipher with a shift as 6 as in server program
def encrypt_caesar(text, shift=6):
    result = ""
    for c in text:
        if c.isalpha():
            base = ord("A") if c.isupper() else ord("a")
            result += chr((ord(c) - base + shift) % 26 + base)
        else:
            result += c
    return result

# All incoming data must be decrypted 
def decrypt_caesar(text, shift=6):
    result = ""
    for c in text:
        if c.isalpha():
            base = ord("A") if c.isupper() else ord("a")
            result += chr((ord(c) - base - shift) % 26 + base)
        else:
            result += c
    return result

# --- Main Loop ---
def main():
    # Manually entering the IP for flexibility 
    esp_ip = input("Enter ESP32 IP address: ").strip()
    port = 5001 # Defined in server program 
    
    # Create a TCP socket on IPV4
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.connect((esp_ip, port)) # Connect to the server
        print(f"Connected to ESP32 at {esp_ip}:{port}\n")
    except Exception as e:
        print("Connection failed:", e)
        return

    """
    Waits for a promp from the server which includes ':' letting the client know that the server is expecting
    input. 
    """
    def wait_for_prompt():
        while True:
            msg = receive()
            if not msg:
                break
            if msg.endswith(":"):
                return msg

    # Receives and decryptes messages from server and handles disconnections 
    def receive():
        try:
            data = s.recv(256).decode()
            if data:
                decrypted = decrypt_caesar(data.strip())
                print("Server:", decrypted)
                return decrypted
            return ""
        except Exception as e:
            print("Connection error:", e)
            s.close()
            return ""

    def send(msg): # all outbound messages go through a cypher
        encrypted = encrypt_caesar(msg)
        s.send(encrypted.encode())

    # --- Communication loop ---
    while True:
        msg = wait_for_prompt()
        # Break if server terminates connection or error occurs
        if not msg or "Goodbye" in msg or "Connection error" in msg:
            break

        """
        Determine appropriate response based on prompt. This is what the other program references.
        When "Enter a new message to display: " is sent from the server this program detects this and adds
        a secret tag, that way if a hacker was to communicate with the server on their own program, it would allow
        them to register and create a password but since their code wouldnt have this block below adding this tag,
        server should just disconnect them and not allow the sign/matrix to be manipulated. 
        """
        if "Enter a new message to display:" in msg:
            text = input("> Enter message to scroll: ").strip()
            # Add the tag the server is expecting for security when manipulating the "sign"
            if not text.startswith("MSG:"):
                text = "MSG:" + text
            send(text)

        elif msg.startswith("Change again?"):
            reply = input("> Would you like to change it again? (Y/N): ").strip().upper()
            send(reply) # send the Y/N response
        else:
            response = input("> ").strip()
            send(response)

    print("\nDisconnected from ESP32.")
    s.close()

if __name__ == "__main__":
    main()



