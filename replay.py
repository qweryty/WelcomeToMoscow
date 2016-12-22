#!/bin/env python3
import argparse
import telnetlib
import time
from datetime import datetime
from xml.dom.minidom import parseString
import pdb
import sys, select


####################################################################################################
# Global constants

# delay during steps in seconds
g_auto_walk_delay = 1
g_swap_latlng = False
g_count = -1
g_coords = []
g_telnet = None



####################################################################################################
# Unportable stupid functions to read with timeout
g_read_with_timeout = None

def read_with_timeout_windows():
    import msvcrt
    start_time = time.time()
    input = ''
    while True:
        if msvcrt.kbhit():
            ch = msvcrt.getche()
            if ord(ch) == 13: # enter_key
                break
            elif ord(ch) >= 32: #space_char
                data += ch
        if len(data) == 0 and (time.time() - start_time) > g_auto_walk_delay:
            break
    if len(data) > 0:
        return data
    else:
        return None

def read_with_timeout_linux():
    i, o, e = select.select( [sys.stdin], [], [], g_auto_walk_delay )
    if (i):
        return sys.stdin.readline().strip()
    else:
        return None

import os
if os.name == 'nt':
    # Windows
    g_read_with_timeout = read_with_timeout_windows
else:
    # other (unix)
    g_read_with_timeout = read_with_timeout_linux

####################################################################################################
# UTIL functions
def parse_args():
    parser = argparse.ArgumentParser(description='Reproduce and simulate a GPS track.')
    parser.add_argument('--file', help='File containing the KML track', required=True)
    parser.add_argument('--port', type=int, help='Telnet port where the emulator is listening to', default=5554)
    parser.add_argument('--interval', type=float, help='Interval between commands, in seconds', default=1)
    parser.add_argument('--start_with', type=int, help='Start at coordinate point', default=0)
    parser.add_argument('--swap', help='Swap latitude and longitude values.', action='store_const', const=True, default=False)
    return parser.parse_args()

def setup_auto_walk(filepath):
    global g_coords
    kml_data = None
    with open(filepath, 'r') as f:
        kml_data = f.read()
    xml = parseString(kml_data)
    placemarks = xml.getElementsByTagName('Placemark')
    for plmark in placemarks:
        coordinates = plmark.getElementsByTagName('coordinates')
        if coordinates is None:
            continue
        coords = coordinates[0].childNodes[0].nodeValue.split('\n')
        # drop zero length splits at start and end
        trimmed = [c.strip() for c in coords if len(c.strip()) > 1]
        for xy in trimmed:
            # note: x,y are returned as pair of STRINGS
            x,y = xy.split(',')
            g_coords.append((x.strip(), y.strip()))
    print("total qty of coords: "+str(len(g_coords)))

def open_telnet(port):
    global g_telnet
    g_telnet = telnetlib.Telnet('127.0.0.1', port)
    g_telnet.read_until(b"OK")

    # CHANGE THIS TO YOUR OWN TOKEN
    auth_cmd = "auth hcREGEnNQxKiAepa".encode("ascii")

    g_telnet.write(auth_cmd + b"\r\n")
    g_telnet.read_until(b"OK")

####################################################################################################
# GEOFIX commands
def send_geofix(lat, lng):
    latlng = [lat, lng]
    geo_cmd = ('geo fix ' + (' '.join(latlng))).encode('ascii')
    print(geo_cmd.decode("ascii"))
    g_telnet.write(geo_cmd + b"\r\n")
    g_telnet.read_until(b"OK")

def set_coords():
    coords = input("Enter coordinates:")
    x,y = coords.split(' ')
    send_geofix(x, y)

def snap_geofix():
    global g_count
    latlng = g_coords[g_count]
    if g_swap_latlng:
        latlng[0], latlng[1] = latlng[1], latlng[0]
    send_geofix(latlng[0], latlng[1])

def inc_g_count(i):
    global g_count
    g_count += i
    if (g_count >= len(g_coords)):
        # stay at last spot
        g_count = len(g_coords) - 1
    elif (g_count < -len(g_coords)):
        # stay at first spot
        g_count = -len(g_coords)

def auto_walk():
    while(True):
        if (g_read_with_timeout() != None):
            return
        # Step once
        inc_g_count(1)
        snap_geofix()

def go_next():
    inc_g_count(1)
    snap_geofix()

def go_prev():
    inc_g_count(-1)
    snap_geofix()

def go_first():
    global g_count
    g_count = 0
    snap_geofix()

def set_delay():
    global g_auto_walk_delay
    time_seconds = input("Enter time in seconds:")
    g_auto_walk_delay = int(time_seconds)

def find_cmd(cmd_map, cmd):
    for k in cmd_map.keys():
        if (k == cmd) or (k.find("[" + cmd + "]") > -1):
            return cmd_map[k]
    return None

def quit():
    global g_telnet
    g_telnet.close()
    sys.exit(0)

def run(args):
    global g_swap_latlng
    g_swap_latlng = args.swap
    open_telnet(args.port)
    setup_auto_walk(args.file)

    func = None
    while(True):
        print("Choose command: " + ', '.join(cmd_map.keys()))
        user_input = input()
        if (user_input == "") and func != None:
            # if only enter is pressed, repeat last command
            func()
            continue
        func = find_cmd(cmd_map, user_input)
        if func == None:
            print("No such command.")
            continue
        func()


cmd_map = {
        "set_[c]oords" : set_coords
        , "set_[t]ime_delay" : set_delay
        , "go_[a]uto" : auto_walk
        , "go_[n]ext" : go_next
        , "go_[p]rev" : go_prev
        , "go_[f]irst" : go_first
        , "go_[s]nap_current" : snap_geofix
        , "[q]uit" : quit
}
def main(argv=None):
    g_args = parse_args()
    run(g_args)


if __name__ == "__main__":
    main()
