**get all meals**
```
curl --location 'http://localhost:8080/topjava/rest/meals/'
```
---
**get meal by id**

```
curl --location 'http://localhost:8080/topjava/rest/meals/100007'
```

---
**delete meal by id**

```
curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100007'
```

---
**get meals within a date and time range**

```
curl
--location 'http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-31&startTime=10%3A00%3A00&endDate=2020-01-31&endTime=23%3A00%3A00'
```

---

**update meal by id**
```
curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100009' \
--header 'Content-Type: application/json' \
--data ' 
    {
        "id": 100009,
        "dateTime": "2020-01-31T20:00:00",
        "description": "Обновили Ужин",
        "calories": 100
    }'
```

---
**create new meal**

```
curl --location 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '    
    {
        "dateTime": "2020-01-21T20:00:00",
        "description": "Новый Ужин",
        "calories": 100
    }'
```