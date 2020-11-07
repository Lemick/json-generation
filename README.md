# Json-Generator

A little Spring-Boot server I developed for generating thousands of JSON documents easily with a template language.  
Many of good projects already exist, but I want to improve mine with the features I want.  
A deployed version can be found here: https://json.iluv.tech (the first API call can be slow because of heroku)  

# Features
- JSON Generation with a templating language
- JS Support in templating (very slow for the moment because interpreted server-side)
- API Endpoint for sending results to an ElasticSearch DB

# Examples
##### A simple case with all functions 

Request  
```/generate?size=1```  

```json
{
    "id": "{{randInt()}}",
    "firstName": "{{randFirstName()}}",
    "lastName": "{{randLastName()}}",
    "email": "{{randEmail()}}",
    "phone": "{{randPhone()}}",
    "age": "{{randInt(0, 99)}}",
    "genre": "{{randEnum(male, female, other)}}",
    "generatedDate": "{{now()}}",
    "isVip": "{{randBool()}}",
    "orderDate": "{{randDate(2019-01-01T00:00:00Z, 2019-12-30T23:59:59Z)}}",
    "customEmail": "{{randFirstName()}}.{{randLastName()}}@gmail.com"
}
```
Response:
```json
[
    {
        "id": 1432509686,
        "firstName": "Allmond",
        "lastName": "Kealey",
        "email": "R.Yee@yahoo.com.br",
        "phone": "+382209767060",
        "age": 36,
        "genre": "other",
        "generatedDate": "2020-11-04T07:50:02.843677Z",
        "isVip": true,
        "orderDate": "2019-11-13T19:55:08Z",
        "customEmail": "Farfan.Kealey@gmail.com"
    }
]
```

##### Nesting Objects

Request 
```json
{
    "id": "{{randInt()}}",
    "person": {
        "firstName": "{{randFirstName()}}",
        "lastName": "{{randLastName()}}"
    }
}
```
Response:
```json
[
    {
        "id": 1279076715,
        "person": {
            "firstName": "Bouie",
            "lastName": "Meacham"
        }
    }
]
```



##### Using a JS Script

You can declare an anonymous function in a field with the following syntax:  
Request 
```json
{
    "js": "{{function() { return 'Hello world ' + 'In JS' }}}"
}
```
Response:
```json
[
    {
        "js": "Hello world In JS"
    }
]
```

***Note***: this functionality use the GraalVM polyglot API, it is very slow for the moment.
