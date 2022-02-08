#!/bin/bash
sudo apt update
sudo apt install python3.9
apt install python3.9-venv
apt install python3-pip
python3.9 -m venv master
source ./master/bin/activate
pip3 install python-dotenv
pip3 install psycopg2-binary
deactivate