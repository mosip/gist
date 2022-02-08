import os
from datetime import datetime

# Maven local repo
from pathlib import Path

home = str(Path.home())

# Env file path
env_path = os.path.abspath(
    os.path.join(os.path.abspath(Path()), '.env')
)

log_file_name = datetime.strftime(datetime.now(), "%Y%m%d")

log_path = os.path.abspath(
    os.path.join(os.path.abspath(Path()), "logs", log_file_name + '.log')
)

generatedDataFolderPath = os.path.abspath(
    os.path.join(os.path.abspath(Path()), 'output')
)

# result paths
vid_list_path = os.path.join(generatedDataFolderPath, 'vidList.json')
credential_data_path = os.path.join(generatedDataFolderPath, 'ridList.json')
result_path = os.path.join(generatedDataFolderPath, 'result.csv')
