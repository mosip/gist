import csv
import os

def write_csv_file(path, dict_data):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    keys = dict_data[0].keys()
    with open(path, 'w', newline='') as file:
        dict_writer = csv.DictWriter(file, keys)
        dict_writer.writeheader()
        dict_writer.writerows(dict_data)



