#url="http://Springusers-env.eba-macic9ka.eu-central-1.elasticbeanstalk.com/auth/tasks"
#Get all tasks
USERNAME="2"
PASSWORD="2"
URL="http://Springusers-env.eba-macic9ka.eu-central-1.elasticbeanstalk.com/auth/tasks"
OPTIONS="-s -X GET -u $USERNAME:$PASSWORD"
RESPONSE=$(curl $OPTIONS $URL)
echo $RESPONSE

#Get task by id
taskId=3
URL="http://Springusers-env.eba-macic9ka.eu-central-1.elasticbeanstalk.com/auth/tasks/$taskId"
OPTIONS="-s -X GET -u $USERNAME:$PASSWORD"
RESPONSE=$(curl $OPTIONS $URL)
echo $RESPONSE

#Delete task by id
taskId=3
URL="http://Springusers-env.eba-macic9ka.eu-central-1.elasticbeanstalk.com/auth/tasks/$taskId"
OPTIONS="-s -X DELETE -u $USERNAME:$PASSWORD"
RESPONSE=$(curl $OPTIONS $URL)
echo $RESPONSE

#Post task
URL="http://Springusers-env.eba-macic9ka.eu-central-1.elasticbeanstalk.com/auth/tasks"
#curl -X POST -H "Content-Type: application/json" -d "$data" $url
data='{
   "id": 0,
   "description": "string21",
   "dueDate": "2024-03-17",
   "status": "PLANNED",
   "userId": 1
}'
OPTIONS="-s -X POST -d '$data' -u $USERNAME:$PASSWORD"
RESPONSE=$(curl $OPTIONS $URL)
echo $RESPONSE






