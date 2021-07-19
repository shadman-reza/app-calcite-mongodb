#!/bin/bash

mongoimport --db demo --collection zips --file /mongo_data/zips.json
mongoimport --db demo --collection population --file /mongo_data/population.json