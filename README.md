# README #

[![Build Status](https://travis-ci.org/thenetcircle/dino-android-framework.svg?branch=master)](https://travis-ci.org/thenetcircle/dino-android-framework)

This Framework is designed to allow for connections to a Dino server

This project is still in prototype development and is also being used as a personal introduction to Kotlin, improvements and changes will be made regularly to protect against screw-ups on my end.

More information about Dino is available at https://thenetcircle.github.io/dino/

### How do I get set up? ###

Currently, the UI is limited and only works to connect and disconnect, the hardcoded access token is the last working access token using the curl request shown below. to use the project, you may need to call the api below (internal TNC staff only currently)

User ID = 179677

	curl -k -X "POST" "https://red.tianwen.php7.hallokoko.lab/api_dev.php/v2/auth/access_token.json"      -H 'Cookie: session_id=1a6ed908d2cd75b8734f8cdad96c98313ecbb6a2'      -H 'Content-Type: application/json; charset=utf-8'      -d $'{
  		"username": "kkk@flirten.de",
  		"password": "121212",
  		"client_id": "koko",
  		"grant_type": "password"
	}'
or for User ID = 9

	curl -k -X "POST" "https://red.tianwen.php7.hallokoko.lab/api_dev.php/v2/auth/access_token.json"      -H 'Cookie: session_id=1a6ed908d2cd75b8734f8cdad96c98313ecbb6a2'      -H 'Content-Type: application/json; charset=utf-8'      -d $'{
        "username": "sam@thenetcircle.com",
        "password": "111111",
        "client_id": "koko",
        "grant_type": "password"
    }'


### Who do I talk to? ###

Please speak to Aaron - aaron@thenetcircle.com / github@aaron-stanley.me
