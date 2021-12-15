#!/usr/bin/env python3

import argparse
import os, atexit
import textwrap


import signal
import random
import time
from enum import Enum

from collections import defaultdict, OrderedDict

def check_positive(value):
    ivalue = int(value)
    if ivalue <= 0:
        raise argparse.ArgumentTypeError("{} is an invalid positive int value".format(value))
    return ivalue


def checkProcess(proc, impactingProcessesForProc, filePath, files):
    i = 1
    nextMessage = defaultdict(lambda : 1)
    filename = os.path.basename(filePath)
    print(files)
    last_deliver_of_each = {}
    with open(filePath) as f:
        for lineNumber, line in enumerate(f):
            tokens = line.split()

            # Check broadcast
            if tokens[0] == 'b':
                msg = int(tokens[1])
                if(not verifyBroadcast(proc, msg, last_deliver_of_each, files)):
                    return False
                

            # Check delivery
            if tokens[0] == 'd':
                sender = int(tokens[1])
                msg = int(tokens[2])
                if sender in impactingProcessesForProc:
                    last_deliver_of_each[sender] = msg

    return True

def verifyBroadcast(proc, broadcast, last_deliver_of_each, files):

    print(f"Broadcast of message {broadcast} by process {proc} depends on {last_deliver_of_each}")
        
    if not last_deliver_of_each:
        return True

    for filePath in files:
        nb_found = 0 
        with open(filePath) as f:
            for lineNumber, line in enumerate(f):
                tokens = line.split()

                # Check delivery
                if tokens[0] == 'd':
                    sender = int(tokens[1])
                    msg = int(tokens[2])
                    if sender in last_deliver_of_each :
                        if last_deliver_of_each[sender] == msg:
                            nb_found += 1

                    if sender == proc and msg == broadcast:
                        if nb_found != len(last_deliver_of_each):
                            return False
                    

    return True
if __name__ == "__main__":
    parser = argparse.ArgumentParser()

    parser.add_argument(
        "--proc_num",
        required=True,
        type=check_positive,
        dest="proc_num",
        help="Total number of processes",
    )

    parser.add_argument('config_path')
    parser.add_argument('output', nargs='+')

    results = parser.parse_args()

    if len(results.output) != results.proc_num:
        print("Not as many output files as number of processes")
        exit(1)

    dict_impacting_processes = {}

    with open(results.config_path) as f:
        for lineNumber, line in enumerate(f):
            tokens = line.split()

            if lineNumber > 0:
                dict_impacting_processes[int(tokens[0])] = list(map(lambda x: int(x), tokens))

        print(dict_impacting_processes)

    for proc, o in enumerate(results.output):
        print(proc)
        print("Checking {}".format(o))
        if not checkProcess(proc + 1, dict_impacting_processes[proc + 1], o, results.output):
            print("Validation failed!")
        else:
            print("Validation OK")



