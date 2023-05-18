# importing the requests library
import requests

# defining the api-endpoint
TOKEN_ENDPOINT = "https://stageauth.equilend.com/auth/realms/1Source/protocol/openid-connect/token"

# data to be sent to api
data = {'client_id':'IntegrationTestClient',
        'client_secret':'9f452b99-2c14-4ec5-a92b-db6d9d52ee5f',
        'grant_type':'password',
        'username':'bnyuser1',
        'password':'s5hsZZnPh0G9'}

# sending post request and saving response as response object
r = requests.post(url = TOKEN_ENDPOINT, data = data)

# extracting response text
token = r.json()
print(token['access_token'])
