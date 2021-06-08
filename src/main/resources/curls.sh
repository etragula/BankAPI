#curl --header 'Content-Type: application/json' --request POST --data '{"accountNumber": "40817810216341000001"}' http://localhost:8080/user/createCard
#curl --header 'Content-Type: application/json' --request POST --data '{"accountNumber": "40817810216341000001", "amount": "6666.999"}' http://localhost:8080/user/deposit
curl --header 'Content-Type: application/json' --request POST --data '{"name": "Mikhail Petrosyan", "phone": "+79774943692"}' http://localhost:8080/office/addNewIndParty
#curl --header 'Content-Type: application/json' --request POST --data '{"phone": "+79774943692"}' http://localhost:8080/office/addNewAccount
#curl --header 'Content-Type: application/json' --request POST --data '{"id": "9"}' http://localhost:8080/office/addNewAccount