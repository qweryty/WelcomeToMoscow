#!/bin/env python3
import argparse
import telnetlib
import time
from datetime import datetime
from xml.dom.minidom import parseString
import pdb

# Author sole http://soledadpenades.com

def parse_args():
    parser = argparse.ArgumentParser(description='Reproduce and simulate a GPS track.')
    parser.add_argument('--file', help='File containing the KML track', required=True)
    parser.add_argument('--port', type=int, help='Telnet port where the emulator is listening to', default=5554)
    parser.add_argument('--interval', type=float, help='Interval between commands, in seconds', default=1)
    parser.add_argument('--start_with', type=int, help='Start at coordinate point', default=0)
    parser.add_argument('--swap', help='Swap latitude and longitude values.', action='store_const', const=True, default=False)
    return parser.parse_args()


def run(args):
    with open(args.file, 'r') as f:
        kml_data = f.read()

    tn = telnetlib.Telnet('127.0.0.1', args.port)
    tn.read_until(b"OK")

    # CHANGE THIS TO YOUR OWN TOKEN
    auth_cmd = "auth hcREGEnNQxKiAepa".encode("ascii")

    tn.write(auth_cmd + b"\r\n")
    tn.read_until(b"OK")

    xml = parseString(kml_data)
    i = 0
    d0 = None
    interval = 0
    realtime = args.realtime

    placemarks = xml.getElementsByTagName('Placemark')
    print("Got %d place marks" % len(placemarks))

    for placemark in placemarks:

        coordinates = placemark.getElementsByTagName('coordinates')
        if coordinates is None:
            continue

        coords = coordinates[0].childNodes[0].nodeValue.split('\n')
        # drop zero length splits at start and end
        coords = [x for x in coords if len(x) > 1]

        print("This placemark has %d steps" % len(coords))
        for xy in coords[i:]:
            latlng = xy.split(',')

            if args.swap:
                latlng[0], latlng[1] = latlng[1], latlng[0]
            geo_cmd = ('geo fix ' + (' '.join(latlng))).encode('ascii')

            if i > args.start_with:
                interval = args.interval

            time.sleep(interval)

            print(str(i+1) + geo_cmd.decode("ascii"))
            tn.write(geo_cmd + b"\r\n")
            tn.read_until(b"OK")

            i += 1

    tn.close()


def main(argv=None):
    args = parse_args()
    run(args)


if __name__ == "__main__":
    main()
