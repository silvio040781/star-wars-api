# API

Requisitos:

Nossos associados são aficionados por Star Wars e com isso, queremos criar um jogo com algumas informações da franquia.

Para possibilitar a equipe de front criar essa aplicação, queremos desenvolver uma API que contenha os dados dos planetas, que podem ser obtidas pela API pública do Star Wars (https://swapi.co/)

Requisitos:

A API deve ser REST
Para cada planeta, os seguintes dados devem ser obtidos do banco de dados da aplicação, que foram inseridos manualmente pela funcionalidade de adicionar planetas:
Nome
Clima
Terreno
Para cada planeta também devemos ter a quantidade de aparições em filmes que deve ser obtida pela api do Star Wars na inserção do planeta.


Funcionalidades desejadas:

Adicionar um planeta (com nome, clima e terreno)
Listar planetas do banco de dados
Listar planetas da API do Star Wars
Buscar por nome no banco de dados
Buscar por ID no banco de dados
Remover planeta

--------------------------------------
Funcionalidades:

POST: Adicionar novo Planeta - http://localhost:8080/api/planetas
Body:
{
	"nome": "Marte",
	"clima": "Seco",
	"terreno": "não conhecido"
}

GET: Retorna Planeta pelo ID - http://localhost:8080/api/planetas/1

GET: Retorna Planeta pelo Nome - http://localhost:8080/api/planetas/?nome=Marte

GET: Listar todos Planetas da API SWAPI - http://localhost:8080/api/planetas/searchAllPlanetasApiSwapi

GET: Listar todos os Planetas do banco de dados - http://localhost:8080/api/planetas/searchAll

DELETE: Remove o planeta do banc de dados - http://localhost:8080/api/planetas/1
