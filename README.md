# README #

This Framework is designed to allow for connections to the Dino framework

More information about Dino is available at https://thenetcircle.github.io/dino/

### How do I get set up? ###

Currently, the UI does not exist, you will need to hardcode and access token using the following curl request in terminal

	curl -k -X "POST" "https://red.tianwen.php7.hallokoko.lab/api_dev.php/v2/auth/access_token.json"      -H 'Cookie: session_id=1a6ed908d2cd75b8734f8cdad96c98313ecbb6a2'      -H 'Content-Type: application/json; charset=utf-8'      -d $'{
  		"username": "kkk@flirten.de",
  		"password": "121212",
  		"client_id": "koko",
  		"grant_type": "password"
	}'


### Who do I talk to? ###

Please speak to Aaron - aaron@thenetcircle.com
