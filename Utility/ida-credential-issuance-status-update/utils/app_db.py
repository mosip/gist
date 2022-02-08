import psycopg2
from psycopg2.extras import RealDictCursor

class DatabaseSession: 
    def __init__(self, user, pwd, host, port, db_name):
        self.conn = psycopg2.connect(user = user, password = pwd, host = host, port = port, database = db_name) 
    
    def close(self):
        self.conn.close()

    def cursor(self):
        return self.cursor()

    def fetch_all(self, query, params=None):
        cur = self.conn.cursor()
        if (params is None):
            cur.execute(query)
        else:
            cur.execute(query.format(params))
        resp = cur.fetchall()   
        cur.close()
        return resp

    def fetch_one(self, query, params=None):
        cur = self.conn.cursor()
        if (params is None):
            cur.execute(query)
        else:
            cur.execute(query, params)
        resp = cur.fetchone()   
        cur.close()
        return resp        

    def update_query(self, query, params=None):
        # print(query, params)
        cur = self.conn.cursor()
        if (params is None):
            cur.execute(query)
        else:
            cur.execute(query, params)
        # print('cur.rowcount', cur.rowcount)
        self.conn.commit()
        cur.close()

