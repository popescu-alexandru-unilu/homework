# Movie Management API

This REST API provides endpoints for managing movies, directors, and actors in the movie database.

## Base URL
All API endpoints are prefixed with `/api`

## Authentication
Currently, no authentication is required for API access.

## Endpoints

### Movies

#### GET /api/movies
Retrieve all movies with their directors.

**Response:**
```json
[
  {
    "id": 1,
    "title": "The Shawshank Redemption",
    "year": 1994,
    "genre": "Drama",
    "runtimeMin": 142,
    "plotOneLine": "Two imprisoned men bond over a number of years...",
    "posterUrl": "https://...",
    "imdbId": "tt0111161",
    "director": {
      "id": 1,
      "firstName": "Frank",
      "lastName": "Darabont",
      "birthDate": "1959-01-28"
    },
    "actors": [
      {
        "id": 1,
        "firstName": "Tim",
        "lastName": "Robbins",
        "birthDate": "1958-10-16"
      }
    ]
  }
]
```

#### GET /api/movies/{id}
Retrieve a specific movie with full details including director and actors.

**Parameters:**
- `id` (path): Movie ID

**Response:** Single movie object (same format as above)

**Error Responses:**
- `404`: Movie not found

#### GET /api/movies/year/{year}
Retrieve all movies from a specific year.

**Parameters:**
- `year` (path): Release year

**Response:** Array of movie objects

#### POST /api/movies
Create a new movie.

**Request Body:**
```json
{
  "title": "New Movie Title",
  "year": 2023,
  "genre": "Action",
  "runtimeMin": 120,
  "plotOneLine": "Movie description...",
  "posterUrl": "https://...",
  "imdbId": "tt1234567",
  "director": {
    "id": 1
  },
  "actors": [
    {"id": 1},
    {"id": 2}
  ]
}
```

**Required Fields:**
- `title`
- `year`
- `director.id`

**Response:** Created movie object with generated ID

**Error Responses:**
- `400`: Missing required fields

### Directors

#### GET /api/directors
Retrieve all directors.

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "Frank",
    "lastName": "Darabont",
    "birthDate": "1959-01-28"
  }
]
```

#### GET /api/directors/{id}
Retrieve a specific director.

**Parameters:**
- `id` (path): Director ID

**Response:** Single director object

**Error Responses:**
- `404`: Director not found

### Actors

#### GET /api/actors
Retrieve all actors.

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "Tim",
    "lastName": "Robbins",
    "birthDate": "1958-10-16"
  }
]
```

#### GET /api/actors/{ids}
Retrieve specific actors by their IDs.

**Parameters:**
- `ids` (path): Comma-separated list of actor IDs (e.g., "1,2,3")

**Response:** Array of actor objects

**Error Responses:**
- `400`: Invalid ID format or no IDs provided

## Error Handling

All endpoints return appropriate HTTP status codes:
- `200`: Success
- `201`: Created
- `400`: Bad Request
- `404`: Not Found
- `500`: Internal Server Error

Error responses include a JSON message describing the error.

## Content Type

All requests and responses use `application/json` content type.

## Example Usage

```bash
# Get all movies
curl http://localhost:8080/Exercise1/api/movies

# Get movie by ID
curl http://localhost:8080/Exercise1/api/movies/1

# Get movies by year
curl http://localhost:8080/Exercise1/api/movies/year/1994

# Create a new movie
curl -X POST http://localhost:8080/Exercise1/api/movies \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Movie",
    "year": 2023,
    "director": {"id": 1}
  }'

# Get all directors
curl http://localhost:8080/Exercise1/api/directors

# Get actors by IDs
curl http://localhost:8080/Exercise1/api/actors/1,2,3
