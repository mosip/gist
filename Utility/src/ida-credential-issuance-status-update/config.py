import os

# Server Info
server = os.getenv("server_url")

# Database Info
db_host = os.getenv("db_host")
db_port = os.getenv("db_port")
db_user = os.getenv("db_user")
db_pass = os.getenv("db_pass")

# Common Info
logger_level = os.getenv("logger_level")

get_cred_txn_query="SELECT id FROM credential.credential_transaction where request like '%mpartner-default-auth%' and DATE(cr_dtimes) = now()::timestamp::DATE"