docker exec -i mysql /bin/bash -c "mysqldump -uroot -padmin@123 --databases money-tracker" > ./money-tracker_databases_20241106.sql

docker exec -i mysql /bin/bash -c "mysqldump -uroot -padmin@123 --no-data --databases money-tracker" > ./money-tracker_structure_20241106.sql