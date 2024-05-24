var querystring = require('querystring');
var https = require('https');

function getAuthToken() {

	var post_data = querystring.stringify({
		'client_id': 'IntegrationTestClient',
		'client_secret': '9f452b99-2c14-4ec5-a92b-db6d9d52ee5f',
		'grant_type': 'password',
		'username': 'bnyuser1',
		'password': 's5hsZZnPh0G9'
	});

	var post_options = {
		hostname: 'stageauth.equilend.com',
		path: '/auth/realms/1Source/protocol/openid-connect/token',
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
			'Content-Length': Buffer.byteLength(post_data)
		}
	};
	
	var post_req = https.request(post_options, function(res) {
      res.setEncoding('utf8');
      res.on('data', function (chunk) {
	      var obj = JSON.parse(chunk);
          console.log(obj.access_token);
      });
  });

  // post the data
  post_req.write(post_data);
  post_req.end();

}

getAuthToken();