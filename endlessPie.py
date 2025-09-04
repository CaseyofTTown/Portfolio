import msvcrt  # Windows-only
import time
#Endlessly print the next value of pi to the console until q is pressed 
def pi_digits():
    """Generator for digits of π using the spigot algorithm (base 10)."""
    q, r, t, k, n, l = 1, 0, 1, 1, 3, 3
    while True:
        if 4*q + r - t < n*t:
            yield n
            q, r, t, k, n, l = (
                10*q,
                10*(r - n*t),
                t,
                k,
                (10*(3*q + r)) // t - 10*n,
                l
            )
        else:
            q, r, t, k, n, l = (
                q*k,
                (2*q + r)*l,
                t*l,
                k + 1,
                (q*(7*k + 2) + r*l) // (t*l),
                l + 2
            )

print("Press 'q' to stop streaming π digits...\n")

stream = pi_digits()
count = 0

while True:
    digit = next(stream)
    print(digit, end='', flush=True)
    count += 1
    time.sleep(0.05)  # Slow it down for visibility
    if msvcrt.kbhit() and msvcrt.getch() == b'q':
        print("\n\n[SYSTEM] π stream manually terminated.")
        print(f"[STATUS] Digits printed: {count}")
        break

