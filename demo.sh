echo "Demo using JSON"

echo "Usage of service in version 1.0 -------------------------------------------------------------"
echo
echo "Creating new message using service v1.0"
curl -i -H 'Accept: application/vnd.messages-v1+json' -d "title='message 1'" http://localhost:8080/messages/create
echo

echo
echo "Getting first page of messages using service v1.0"
curl -i -G -H 'Accept: application/vnd.messages-v1+json' -H 'Content-type: application/json'  http://localhost:8080/messages/list/1
echo

echo
echo "Getting second page of messages using service v1.0"
curl -i -G -H 'Accept: application/vnd.messages-v1+json' -H 'Content-type: application/json'  http://localhost:8080/messages/list/2
echo

echo
echo "Usage of service in version 2.0 -------------------------------------------------------------"
echo
echo "Creating new message using service v2.0"
curl -i -H 'Accept: application/vnd.messages-v2+json' -d "title='message 2'&content='content 2'" http://localhost:8080/messages/create
echo

echo
echo "Getting first page of messages using service v2.0"
curl -i -G -H 'Accept: application/vnd.messages-v2+json' -H 'Content-type: application/json'  http://localhost:8080/messages/list/1
echo

echo
echo "Getting second page of messages using service v2.0"
curl -i -G -H 'Accept: application/vnd.messages-v2+json' -H 'Content-type: application/json'  http://localhost:8080/messages/list/2
echo

echo
echo "Using new feature of service v2.0 - getting filtered messages (with filtered fields)"
curl -i -G -H 'Accept: application/vnd.messages-v2+json' -H 'Content-type: application/json' -d "includeFields='content'"  http://localhost:8080/messages/filterList/1
echo

#end
echo