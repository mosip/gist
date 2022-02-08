FROM python:3.10.2-alpine3.15
ADD . /update-credential-status
WORKDIR /update-credential-status
RUN pip3 install python-dotenv
RUN pip3 install psycopg2-binary
CMD python3 main.py