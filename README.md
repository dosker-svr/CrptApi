#Данный набор методов предназачен для работы с API системы маркировки "Честный Знак"

###Получение авторизационного токена
Для получения аутентификационного токена необходимо запросить в TT IS массив случайных данных
(GET /api/v3/auth/cert/key), подписать их КЭЧ и отправить в ИС ТТ для проверки
(POST /api/v3/auth/cert/). В случае успешной проверки подписи ИС ТТ возвращает токен аутентификации в формате jwt (см. jwt.io)
![img.png](img_for_readme/img.png)

###Создание документа
####Запрос
![img_1.png](img_for_readme/img_1.png)

####Ответ
![img.png](img_for_readme/img_2.png)

###Выпуск в оборот. Контракт производство в Российской Федерации
####Запрос
![img.png](img_for_readme/img_3.png)
####Ответ
![img.png](img_for_readme/img_2.png)
