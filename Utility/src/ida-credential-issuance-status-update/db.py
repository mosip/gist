import config as conf
from utils.app_db import DatabaseSession

def getDBSession(db_name):
    db = DatabaseSession(
                user=conf.db_user, 
                pwd=conf.db_pass, 
                host=conf.db_host, 
                port=conf.db_port, 
                db_name=db_name)
    return db

def get_credential_txn(db_conn):
    try:
        query = conf.get_cred_txn_query
        cred_txn_ids = db_conn.fetch_all(query)
        return cred_txn_ids
    except Exception as ex:
        raise Exception(f"while getting Credential Transactions... {ex}")

def get_credential_stores(db_conn, cred_txn_ids):
    try:
        query = """
        select credential_transaction_id from ida.credential_event_store where credential_transaction_id IN {0}
        """
        params = cred_txn_ids
        return db_conn.fetch_all(query, params)
    except Exception as ex:
        raise Exception(f"while getting credential store - {ex}")

def get_credential_store(db_conn, cred_txn_id):
    try:
        query = """
        select credential_transaction_id from ida.credential_event_store where credential_transaction_id=%s;
        """
        params = cred_txn_id
        result = db_conn.fetch_one(query, params)
        if result is None:
            return []
        print([result])
        return [result]
    except Exception as ex:
        raise Exception(f"while getting credential store - {ex}")

def update_credential_txn(db_conn, id):
    try:
        query = "UPDATE credential.credential_transaction set status_code=%s where id=%s"
        params = ('NEW',id)
        db_conn.update_query(query, params)
    except Exception as ex:
        raise Exception(f"while updating credential transaction - {ex}")

