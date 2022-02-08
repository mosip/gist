import argparse
import sys

from utils.app_path import env_path, log_path, result_path
from dotenv import load_dotenv
from utils.app_logger import init_logger, debug, info, warning, error
from utils.app_csv import write_csv_file

load_dotenv()
# OR, the same with increased verbosity
load_dotenv(verbose=True)
load_dotenv(dotenv_path=env_path)

from db import getDBSession, get_credential_txn, get_credential_store, update_credential_txn, get_credential_stores
from utils.app_helper import time_diff, get_time_in_sec
import config as conf

def main():
    init_logger(log_file=log_path, level=conf.logger_level)
    start_time = get_time_in_sec()
    try:
        prev_time = start_time
        cred_db_conn = getDBSession("mosip_credential")
        ida_db_conn = getDBSession("mosip_ida")
        info("Get Credential Transaction")
        cred_txns = get_credential_txn(cred_db_conn)
        if (len(cred_txns)) > 0:
            cred_txns = [r[0] for r in cred_txns]
            info("Validating Credential Store Existance")
            if len(cred_txns) == 1:
                store_list = get_credential_store(ida_db_conn, cred_txns)
            else:
                store_list = get_credential_stores(ida_db_conn, tuple(cred_txns))

            output_data_list = []
            for cred_txn in cred_txns:
                cred_txn_id = cred_txn
                store_exist = [store for store in store_list if store[0] == cred_txn_id]
                if sum(map(len, store_exist)) == 0:
                    output_data = dict(Credential_Txn_Id=cred_txn_id)
                    info(f"Not exist {cred_txn_id}, so updating the status")
                    update_credential_txn(cred_db_conn, cred_txn_id)
                    output_data["Status"] = "Updated to NEW status"
                    output_data_list.append(output_data)

            if len(output_data_list) == 0:
                output_data_list.append(dict(Credential_Txn_Id="No data found to update"))
            write_csv_file(result_path, output_data_list)
        else:
            info("No records to process")
        prev_time, prstr = time_diff(prev_time)
    except Exception as e:
        prev_time, prstr = time_diff(start_time)
        error(repr(e))
    finally:
        if cred_db_conn is not None:
            cred_db_conn.close()
        if ida_db_conn is not None:
            ida_db_conn.close()            
        prev_time, prstr = time_diff(start_time)
        info("Total time taken by the script: " + prstr)
        sys.exit(0)

def args_parse():
    parser = argparse.ArgumentParser()
    # parser.add_argument('action', help='get_vid|get_rid_by_query|get_rid_by_id|all')
    # parser.add_argument('vid', help="Get RID by VID for get_rid_by_id action")
    # args = parser.parse_args()
    # return args, parser
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument('--get_vid', action='store_true',  help='Query db and get VIDs')
    group.add_argument('--get_rid_by_query', action='store_true',  help='Query db and get RIDs')
    group.add_argument('--get_rid_by_vid', type=str,  help='Get RID by VID')
    group.add_argument('--create_cr_by_query', action='store_true',  help='Create credential transaction by query')
    group.add_argument('--create_cr_by_vid', type=str,  help='Create credential transaction by VID')
    args = parser.parse_args()
    return args, parser

if __name__ == "__main__":
    main()
