# Indian Recipes & "What Should We Cook?" App — Production Plan

## 1. Product Overview

Build a production-ready Android application for Google Play focused on Indian cooking.

Core capabilities:

1. Browse North Indian and South Indian recipes.
2. Categorize recipes by meal:
   - Breakfast
   - Brunch
   - Lunch
   - Snacks
   - Dinner
3. Search and filter recipes.
4. Show nutrition estimates, especially calories and protein per 100 g.
5. Provide an N-player "What Should We Cook?" game where multiple people participate and the app selects/scores recipe choices.
6. Work reliably with an internet connection and gracefully handle offline/retry states.
7. Be production-ready for Google Play release.

The app should be designed so the initial recipe catalog can ship inside the app/database and additional recipes can be added later without changing the application architecture.

---

## 2. Recommended Technology Stack

### Android app

Use:

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- Android Architecture Components
- ViewModel
- Kotlin Coroutines + Flow
- Room for local persistence/cache
- Retrofit + OkHttp for REST APIs
- Kotlin Serialization or Moshi for JSON
- Coil for recipe images
- Hilt for dependency injection

Architecture:

**Clean Architecture + MVVM**

Layers:

```text
UI / Compose
    ↓
ViewModel
    ↓
Use Cases
    ↓
Repository
    ↓
Local Room DB + Remote API
```

Use a single-activity architecture.

### Backend

Recommended:

- Python
- FastAPI
- PostgreSQL
- SQLAlchemy
- Alembic
- Pydantic
- JWT authentication if user accounts are introduced
- Docker

Deploy backend using a production cloud platform such as Render, Railway, AWS, GCP, or Azure.

Do not hard-code provider-specific assumptions into the Android app.

### Image storage

Use object storage/CDN rather than storing large recipe images directly in PostgreSQL.

Examples:

- Cloudflare R2
- AWS S3
- Google Cloud Storage

The database should store image URLs.

### CI/CD

Use GitHub Actions for:

- Android build
- Unit tests
- Backend tests
- Lint
- Release APK/AAB generation

Production Android releases must use an **Android App Bundle (.aab)**.

---

## 3. Application Name

Use a placeholder application name during development:

**Rasoi Royale**

The name, package ID, icon, colors, and branding should be configurable.

Suggested package:

```text
com.example.rasoifood
```

Before publishing, replace it with a unique production package ID.

---

## 4. Primary User Flows

### Flow A — Home

Home screen:

```text
------------------------------------------------
Rasoi Royale

What are you cooking today?

[ Search recipes... ]

Meal Categories
[ Breakfast ] [ Brunch ]
[ Lunch ]     [ Snacks ]
[ Dinner ]

Cuisine
[ North Indian ] [ South Indian ]

Popular Recipes
[Recipe cards...]

[ What Should We Cook? ]
------------------------------------------------
```

Home should provide quick access to the game.

---

### Flow B — Browse Recipes

Filters:

- Cuisine
  - North Indian
  - South Indian
- Meal type
  - Breakfast
  - Brunch
  - Lunch
  - Snacks
  - Dinner
- Vegetarian
- Vegan
- Gluten-free
- High-protein
- Low-calorie
- Preparation time

Sort:

- Popular
- Newest
- Preparation time
- Calories
- Protein

Recipe cards should show:

- Image
- Name
- Cuisine
- Meal type
- Preparation time
- Calories / 100 g
- Protein / 100 g

---

## 5. Recipe Detail Screen

Display:

```text
[Large Recipe Image]

Masala Dosa

South Indian · Breakfast

★★★★★

Prep: 15 min
Cook: 25 min
Serves: 2

Nutrition per 100 g
Calories: 180 kcal
Protein: 4.5 g

Ingredients
----------------
...

Instructions
----------------
1. ...
2. ...
3. ...

[ Add to Favorites ]
[ Start Cooking ]
```

Optional future fields:

- Carbohydrates
- Fat
- Fiber
- Sodium
- Sugar

Nutrition should clearly be labeled as an **estimate** unless verified from a trusted nutritional database or laboratory analysis.

---

## 6. Recipe Data Model

Each recipe should contain at least:

```text
Recipe
- id
- name
- slug
- description
- cuisine
- meal_type
- preparation_time_minutes
- cooking_time_minutes
- servings
- ingredients
- instructions
- image_url
- calories_per_100g
- protein_g_per_100g
- vegetarian
- vegan
- gluten_free
- tags
- difficulty
- created_at
- updated_at
```

Recommended relational structure:

```text
recipes
ingredients
recipe_ingredients
recipe_steps
categories
recipe_categories
favorites
users
game_sessions
game_players
game_votes
```

For MVP, ingredients and instructions may be stored as JSONB in PostgreSQL to simplify development. If advanced ingredient analytics are required later, normalize them.

---

## 7. Nutrition Requirements

The application must not invent nutrition values randomly.

Create a nutrition calculation/data pipeline.

For each ingredient:

```text
Ingredient
- name
- quantity_g
- calories_per_100g
- protein_per_100g
```

Calculate:

```text
ingredient_calories =
quantity_g × calories_per_100g / 100

ingredient_protein =
quantity_g × protein_per_100g / 100
```

Total recipe:

```text
total_calories = Σ ingredient_calories

total_protein = Σ ingredient_protein
```

For a cooked recipe:

```text
calories_per_100g =
total_calories / final_cooked_weight_g × 100

protein_per_100g =
total_protein / final_cooked_weight_g × 100
```

Important:

- Final cooked weight can differ significantly from raw ingredient weight because of water absorption/loss.
- Store `final_cooked_weight_g` where available.
- Clearly mark nutrition as estimated.
- Store the nutritional data source and version.
- Do not claim medical or dietary accuracy.

Create a backend/admin validation mechanism so nutrition values can be reviewed before publication.

---

## 8. Initial Recipe Catalog

The MVP should ship with enough content to make the app useful immediately.

Target:

**At least 100 recipes for the first release.**

Suggested distribution:

### North Indian

Breakfast:
- Aloo Paratha
- Paneer Paratha
- Gobi Paratha
- Poori
- Chole Bhature
- Besan Chilla
- Moong Dal Chilla

Brunch:
- Chole
- Rajma Chawal
- Kadhi Pakora
- Paneer dishes
- Stuffed parathas

Lunch:
- Dal Tadka
- Rajma
- Chole
- Dal Makhani
- Aloo Gobi
- Bhindi Masala
- Palak Paneer
- Shahi Paneer
- Jeera Rice
- Roti
- Naan

Snacks:
- Samosa
- Aloo Tikki
- Pakora
- Dhokla
- Paneer Tikka
- Chaat

Dinner:
- Dal Makhani
- Palak Paneer
- Kadai Paneer
- Mix Veg
- Baingan Bharta
- Dal Fry
- Roti
- Jeera Rice

### South Indian

Breakfast:
- Idli
- Masala Dosa
- Plain Dosa
- Uttapam
- Medu Vada
- Pongal
- Upma
- Pesarattu

Brunch:
- Sambar
- Lemon Rice
- Curd Rice
- Bisibele Bath
- Vegetable Pongal

Lunch:
- Sambar Rice
- Rasam Rice
- Curd Rice
- Lemon Rice
- Tamarind Rice
- Vegetable Biryani
- Avial
- Poriyal

Snacks:
- Medu Vada
- Sundal
- Banana Bajji
- Murukku
- Mysore Bonda

Dinner:
- Dosa
- Idli
- Vegetable Uttapam
- Adai
- Appam
- Vegetable Stew
- Pesarattu

Do not copy recipes or images from copyrighted websites without appropriate rights/licensing. Recipe content should be original, properly licensed, public-domain, or otherwise legally usable.

---

## 9. N-Player Cooking Game

This is a core differentiator.

### Game concept

Name:

**What Should We Cook?**

A host creates a game.

Example:

```text
Players: 4

Host:
Rahul

Players:
Rahul
Priya
Amit
Neha
```

Host chooses:

```text
Meal:
Dinner

Cuisine:
North Indian + South Indian

Optional filters:
Vegetarian
Under 500 kcal / 100 g
High protein
```

The backend generates a set of candidate recipes.

Each player receives the candidate recipes and votes independently.

Example:

```text
Round 1

Choose one:

[ Masala Dosa ]
[ Paneer Tikka ]
[ Rajma Chawal ]
[ Idli Sambar ]
```

Players vote.

The app calculates:

```text
recipe_score =
number_of_votes
```

Optional weighted score:

```text
final_score =
vote_score
+ preference_score
+ diversity_score
```

The highest scoring recipe wins.

### Tie breaker

If two recipes tie:

1. Start a final voting round.
2. If still tied, select randomly.
3. Clearly show that the final selection was randomized.

### Game architecture

For real-time multiplayer:

```text
Android clients
       ↓
FastAPI WebSocket
       ↓
Game Session Manager
       ↓
PostgreSQL
```

Use WebSockets for:

- Player joined
- Player left
- Game started
- New round
- Vote submitted
- Vote count finalized
- Winner announced

Each game has:

```text
game_id
host_id
status
meal_type
cuisine
filters
created_at
expires_at
```

Players:

```text
player_id
game_id
display_name
joined_at
connected
```

Votes:

```text
vote_id
game_id
round_id
player_id
recipe_id
created_at
```

Never expose another player's vote before the voting round ends.

---

## 10. Game UX

### Create Game

```text
What Should We Cook?

Number of players
[-] 4 [+]

Meal
[ Dinner ▼ ]

Cuisine
[x] North Indian
[x] South Indian

Diet
[x] Vegetarian

[ Create Game ]
```

Generate a shareable code:

```text
GAME CODE

R4K9P

[ Copy Code ]
[ Share ]
```

### Join Game

```text
Enter game code

[ R4K9P ]

Your name

[ ______ ]

[ Join Game ]
```

### Lobby

```text
Waiting for players...

✓ Rahul
✓ Priya
✓ Amit
...

[ Start Game ]
```

### Voting

Use swipeable cards or recipe cards.

```text
ROUND 1

What should we cook?

[Recipe]

[ Vote ]
```

### Winner

```text
🎉 Tonight's Winner

PALAK PANEER

4 votes

Calories: ...
Protein: ...

[ View Recipe ]
[ Play Again ]
```

---

## 11. Backend API

Suggested endpoints:

### Recipes

```http
GET /api/v1/recipes
GET /api/v1/recipes/{id}
GET /api/v1/recipes/search?q=
GET /api/v1/recipes?cuisine=north_indian
GET /api/v1/recipes?meal_type=dinner
```

### Categories

```http
GET /api/v1/categories
GET /api/v1/cuisines
```

### Favorites

```http
POST /api/v1/favorites/{recipe_id}
DELETE /api/v1/favorites/{recipe_id}
GET /api/v1/favorites
```

### Games

```http
POST /api/v1/games
GET /api/v1/games/{game_id}
POST /api/v1/games/{game_id}/join
POST /api/v1/games/{game_id}/start
POST /api/v1/games/{game_id}/vote
GET /api/v1/games/{game_id}/result
```

### WebSocket

```text
/ws/games/{game_id}
```

Implement server-side validation for:

- Invalid game IDs
- Duplicate player names where applicable
- Duplicate votes
- Voting after round closure
- Unauthorized host actions
- Expired games
- Malformed input

---

## 12. Authentication

MVP can allow anonymous users for browsing.

For multiplayer:

Option A:
- Anonymous player identity generated locally.

Option B:
- Firebase Authentication.

If accounts are introduced, support:

- Google Sign-In
- Email/password

Do not require registration merely to browse recipes.

---

## 13. Local Offline Strategy

Recipe browsing should remain useful even when the network is unavailable.

On first launch:

```text
Bundled initial recipe catalog
        ↓
Room database
        ↓
Remote synchronization
```

Use repository pattern:

```text
UI
 ↓
Repository
 ↓
Room ←→ API
```

The game requires network connectivity.

Show clear offline messaging rather than failing silently.

---

## 14. Search

MVP search:

- Recipe name
- Ingredient name
- Cuisine
- Meal type

Backend should support pagination:

```text
?page=1&page_size=20
```

Never load the entire recipe catalog into memory.

Use PostgreSQL indexes for:

- slug
- cuisine
- meal_type
- vegetarian
- calories_per_100g
- protein_g_per_100g

For larger scale, introduce PostgreSQL full-text search or Elasticsearch/OpenSearch.

---

## 15. Recommended Android Screens

Create these screens:

```text
SplashScreen
HomeScreen
RecipeListScreen
RecipeDetailScreen
SearchScreen
FavoritesScreen
GameCreateScreen
GameJoinScreen
GameLobbyScreen
GameVotingScreen
GameResultScreen
SettingsScreen
AboutScreen
PrivacyPolicyScreen
```

Navigation:

```text
Home
 ├── Recipes
 │    ├── Search
 │    └── Recipe Detail
 ├── Favorites
 └── What Should We Cook?
      ├── Create Game
      ├── Join Game
      ├── Lobby
      ├── Voting
      └── Result
```

---

## 16. UI/UX Design

Use a modern Indian-food visual identity without overloading the interface with decorative elements.

Requirements:

- Material 3
- Light and dark mode
- Responsive layouts
- Large food photography
- Clear typography
- Accessible contrast
- Minimum touch target ~48dp
- Content descriptions for images
- Screen-reader-friendly controls

Recipe cards:

```text
┌─────────────────────────┐
│                         │
│       FOOD IMAGE        │
│                         │
├─────────────────────────┤
│ Masala Dosa             │
│ South Indian · Dinner   │
│                         │
│ 180 kcal · 4.5g protein │
└─────────────────────────┘
```

---

## 17. Admin / Content Management

Do not require developers to modify application code whenever a recipe changes.

Create an admin API or lightweight admin interface.

Admin features:

- Create recipe
- Edit recipe
- Delete/unpublish recipe
- Upload image
- Assign cuisine
- Assign meal category
- Edit nutrition
- Preview recipe
- Publish/unpublish
- Search recipes

Recipe status:

```text
DRAFT
REVIEW
PUBLISHED
ARCHIVED
```

Only `PUBLISHED` recipes should appear in the public app.

---

## 18. Database Schema

PostgreSQL:

```sql
recipes
-------
id UUID PRIMARY KEY
name VARCHAR
slug VARCHAR UNIQUE
description TEXT
cuisine VARCHAR
meal_type VARCHAR
prep_time_minutes INT
cook_time_minutes INT
servings INT
final_cooked_weight_g FLOAT
calories_per_100g FLOAT
protein_g_per_100g FLOAT
vegetarian BOOLEAN
vegan BOOLEAN
gluten_free BOOLEAN
difficulty VARCHAR
image_url TEXT
nutrition_source TEXT
status VARCHAR
created_at TIMESTAMP
updated_at TIMESTAMP
```

For MVP:

```sql
ingredients JSONB
instructions JSONB
tags JSONB
```

Game:

```sql
game_sessions
-------------
id UUID PRIMARY KEY
code VARCHAR UNIQUE
host_id UUID
status VARCHAR
meal_type VARCHAR
cuisine JSONB
filters JSONB
created_at TIMESTAMP
expires_at TIMESTAMP
```

Players:

```sql
game_players
------------
id UUID PRIMARY KEY
game_id UUID
display_name VARCHAR
created_at TIMESTAMP
```

Votes:

```sql
game_votes
----------
id UUID PRIMARY KEY
game_id UUID
round_id UUID
player_id UUID
recipe_id UUID
created_at TIMESTAMP

UNIQUE(round_id, player_id)
```

---

## 19. Security

Implement:

- HTTPS only in production
- CORS restrictions
- Input validation
- SQL injection protection through ORM/parameterized queries
- Rate limiting on game creation/join endpoints
- Server-side authorization
- WebSocket authentication/validation
- Secure secrets via environment variables
- No API keys committed to Git
- Production database credentials outside source control

Never put PostgreSQL credentials in the Android application.

---

## 20. Privacy

The app should collect the minimum data necessary.

For anonymous game users:

- Display name
- Temporary game identity
- Game activity

Automatically expire old game sessions.

Create:

```text
Privacy Policy
Terms of Use
About
Contact
```

If analytics, advertising, authentication, or third-party SDKs are used, update the privacy policy and Play Console Data Safety declarations accordingly.

---

## 21. Analytics

Use analytics only if needed.

Potential events:

```text
app_open
recipe_view
recipe_search
recipe_favorite
game_created
game_joined
game_started
vote_submitted
game_completed
```

Do not collect unnecessary personally identifiable information.

---

## 22. Testing

### Android unit tests

Test:

- ViewModels
- Use cases
- Repository
- Nutrition display formatting
- Filtering
- Game state transitions

### Backend tests

Test:

- Recipe CRUD
- Search
- Filtering
- Authentication/authorization
- Game creation
- Join
- Voting
- Tie-breaking
- Expiry
- WebSocket events

### Integration tests

Test:

```text
Create game
 → Join players
 → Start game
 → Vote
 → Close round
 → Determine winner
```

### UI tests

Test:

- Home navigation
- Recipe filtering
- Recipe details
- Create game
- Join game
- Voting
- Winner screen

### Release checks

Before release:

```text
./gradlew test
./gradlew lint
./gradlew connectedAndroidTest
```

Backend:

```text
pytest
```

---

## 23. Docker

Backend should include:

```text
backend/
├── Dockerfile
├── requirements.txt / pyproject.toml
├── app/
├── tests/
└── alembic/
```

Provide:

```text
docker-compose.yml
```

for local:

```text
FastAPI
PostgreSQL
```

The production environment should use managed PostgreSQL rather than a database container with persistent local storage.

---

## 24. Repository Structure

Recommended monorepo:

```text
rasoi-royale/
│
├── android/
│   ├── app/
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── gradle/
│
├── backend/
│   ├── app/
│   │   ├── api/
│   │   ├── core/
│   │   ├── models/
│   │   ├── schemas/
│   │   ├── repositories/
│   │   ├── services/
│   │   └── websocket/
│   ├── tests/
│   ├── alembic/
│   ├── Dockerfile
│   └── pyproject.toml
│
├── data/
│   ├── recipes/
│   └── nutrition/
│
├── docs/
│
├── .github/
│   └── workflows/
│
├── docker-compose.yml
├── README.md
└── LICENSE
```

---

## 25. API Environment Configuration

Android:

```text
DEBUG_BASE_URL=http://10.0.2.2:8000/
RELEASE_BASE_URL=https://api.yourdomain.com/
```

Backend environment:

```text
DATABASE_URL=
SECRET_KEY=
CORS_ORIGINS=
IMAGE_STORAGE_BUCKET=
IMAGE_STORAGE_ACCESS_KEY=
IMAGE_STORAGE_SECRET_KEY=
```

Use separate development and production configurations.

---

## 26. Deployment Architecture

Production:

```text
                         ┌─────────────────┐
                         │  Google Play    │
                         └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │ Android App     │
                         │ Kotlin/Compose  │
                         └────────┬────────┘
                                  │ HTTPS
                                  ▼
                         ┌─────────────────┐
                         │ Load Balancer / │
                         │ API Gateway     │
                         └────────┬────────┘
                                  │
                                  ▼
                         ┌─────────────────┐
                         │ FastAPI         │
                         │ Backend         │
                         └──────┬────┬─────┘
                                │    │
                    ┌───────────┘    └────────────┐
                    ▼                             ▼
             ┌─────────────┐               ┌─────────────┐
             │ PostgreSQL  │               │ Object      │
             │             │               │ Storage     │
             └─────────────┘               └─────────────┘
```

For WebSockets, ensure the selected hosting provider supports persistent WebSocket connections.

---

## 27. Domain

Use a production domain such as:

```text
api.<your-domain>.com
```

Do not use the cloud provider's temporary URL as the final public API URL.

Configure:

- DNS
- HTTPS/TLS
- CORS
- Production environment variables

---

## 28. Google Play Store Release

Create a Google Play Console developer account.

Prepare:

- Application name
- App icon
- Feature graphic
- Screenshots
- Short description
- Full description
- Privacy Policy URL
- Support/contact information
- Data Safety form
- Content rating
- Target audience declaration
- App access information if required
- Production AAB

Use Android App Bundle:

```text
app-release.aab
```

Configure signing using a secure upload key/keystore.

Never commit the keystore or passwords to Git.

Use Play App Signing.

Before publishing, verify current Google Play target SDK, permission, privacy, and testing requirements because Google can change these requirements.

---

## 29. Google Play Store Assets

Prepare:

### App icon

- 512 × 512 px
- PNG
- Clean food/cooking identity

### Feature graphic

- 1024 × 500 px

### Screenshots

Prepare phone screenshots showing:

1. Home
2. Recipe browsing
3. Recipe detail
4. Nutrition
5. Game creation
6. Multiplayer lobby
7. Voting
8. Winner

Do not put misleading claims in screenshots.

---

## 30. Monetization — Future Ready

Do not add monetization to MVP unless required.

Possible future options:

- Ads
- Premium recipes
- Premium meal plans
- Sponsored recipes
- Subscription
- Grocery-list integrations

If ads are introduced, use a compliant ad SDK and update Play Console disclosures.

---

## 31. Future Features

Architecture should make these possible:

### Personalized recommendations

```text
user history
     ↓
recipe embeddings / preferences
     ↓
recommendation model
     ↓
personalized recipes
```

### Meal planner

```text
Monday
Breakfast → Idli
Lunch → Rajma Rice
Dinner → Palak Paneer
```

### Grocery list

Convert recipe ingredients into:

```text
Shopping List
- 500 g tomatoes
- 250 g paneer
- 1 kg rice
- coriander
```

### Household voting

Allow recurring groups/families.

### AI cooking assistant

Future conversational interface:

```text
"What can I cook with paneer, spinach and tomatoes?"
```

The AI should retrieve recipes from the application's database rather than hallucinating nutritional information.

---

## 32. MVP Definition

The first production release is complete when all of these work:

### Recipes

- [ ] 100+ recipes
- [ ] North Indian
- [ ] South Indian
- [ ] Breakfast
- [ ] Brunch
- [ ] Lunch
- [ ] Snacks
- [ ] Dinner
- [ ] Search
- [ ] Filters
- [ ] Recipe details
- [ ] Calories / 100 g
- [ ] Protein / 100 g
- [ ] Favorites
- [ ] Images

### Game

- [ ] Create game
- [ ] Join using code
- [ ] N players
- [ ] Lobby
- [ ] Recipe selection
- [ ] Voting
- [ ] Tie breaker
- [ ] Winner screen
- [ ] WebSocket real-time updates

### Engineering

- [ ] Android app
- [ ] FastAPI backend
- [ ] PostgreSQL
- [ ] Room cache
- [ ] Docker
- [ ] Environment configuration
- [ ] Automated tests
- [ ] CI/CD
- [ ] HTTPS
- [ ] Production logging
- [ ] Error handling
- [ ] Privacy policy
- [ ] Google Play assets
- [ ] Signed AAB

---

## 33. Cursor Implementation Instructions

Implement this project incrementally. Do not attempt to generate the entire application in one giant file.

### Phase 1 — Project foundation

Create:

```text
android/
backend/
data/
docs/
```

Set up Git, Gradle, FastAPI, PostgreSQL, Docker, environment configuration and CI.

Deliverable:

```text
Both Android and backend build successfully.
```

### Phase 2 — Recipe backend

Implement:

- Database schema
- Alembic migrations
- Recipe models
- Pydantic schemas
- CRUD APIs
- Search
- Filtering
- Pagination
- Seed data

Deliverable:

```text
GET /api/v1/recipes
GET /api/v1/recipes/{id}
```

working with PostgreSQL.

### Phase 3 — Android recipe experience

Implement:

- Navigation
- Home
- Recipe list
- Filters
- Search
- Detail
- Room cache
- Error/loading/empty states

Deliverable:

```text
User can browse the complete recipe catalog.
```

### Phase 4 — Nutrition

Implement nutrition fields and validation.

Deliverable:

```text
Every published recipe displays calories and protein per 100 g.
```

### Phase 5 — Multiplayer game backend

Implement:

- Game session
- Player management
- WebSocket
- Voting
- Winner logic
- Tie breaker
- Expiry

Deliverable:

```text
Multiple devices can join the same game and vote in real time.
```

### Phase 6 — Multiplayer Android UI

Implement:

- Create game
- Join game
- Lobby
- Voting
- Results
- Share game code

Deliverable:

```text
Complete end-to-end multiplayer experience.
```

### Phase 7 — Production hardening

Implement:

- Security
- Rate limiting
- Logging
- Monitoring
- Error reporting
- Database indexes
- API validation
- Offline behavior
- Automated tests

### Phase 8 — Play Store release

Generate:

```text
release AAB
```

Verify:

- Production API
- HTTPS
- App signing
- Privacy policy
- Data Safety
- Store listing
- Screenshots
- Target SDK
- Release testing

---

## 34. Cursor Rules

When generating code:

1. Prefer small, maintainable files.
2. Do not place all code in `MainActivity.kt`.
3. Follow Clean Architecture.
4. Use dependency injection.
5. Use strongly typed models.
6. Avoid magic strings.
7. Use sealed classes/enums for finite states.
8. Handle loading/error/success states explicitly.
9. Do not hard-code production URLs.
10. Do not hard-code secrets.
11. Add tests alongside major features.
12. Add database migrations for schema changes.
13. Use pagination for recipe APIs.
14. Validate all backend input.
15. Use server-authoritative game state.
16. Never trust vote counts supplied by the Android client.
17. Prevent duplicate votes server-side.
18. Do not expose private votes before a voting round closes.
19. Keep nutrition calculations deterministic and auditable.
20. Do not use copyrighted recipe content/images without appropriate rights.
21. Keep the app functional if optional backend features fail.
22. Use meaningful error messages.
23. Include accessibility semantics in Compose.
24. Use UTC timestamps in backend/database.
25. Make all game sessions expire automatically.
26. Keep development and production configurations separate.

---

## 35. Definition of Done

The project is considered production-ready only when:

```text
Android
  ✓ builds release AAB
  ✓ passes tests
  ✓ passes lint
  ✓ no hard-coded secrets
  ✓ production API configured
  ✓ offline recipe cache works

Backend
  ✓ Docker image builds
  ✓ migrations work
  ✓ PostgreSQL production database works
  ✓ REST API works
  ✓ WebSocket game works
  ✓ tests pass
  ✓ rate limiting/security configured
  ✓ logs and health endpoint work

Content
  ✓ 100+ legally usable recipes
  ✓ nutrition values reviewed
  ✓ images are legally usable
  ✓ recipes categorized correctly

Google Play
  ✓ app signed
  ✓ AAB uploaded
  ✓ store listing completed
  ✓ privacy policy published
  ✓ Data Safety completed
  ✓ content rating completed
  ✓ production testing completed
```

---

## 36. Health Endpoint

Backend should expose:

```http
GET /health
```

Response:

```json
{
  "status": "ok"
}
```

Optionally add:

```http
GET /ready
```

for deployment readiness checks.

---

## 37. API Documentation

FastAPI should expose OpenAPI documentation in development.

Do not expose sensitive infrastructure information in production documentation.

Generate/update:

```text
docs/api.md
```

with endpoint descriptions and example requests.

---

## 38. Error Contract

Use consistent API errors:

```json
{
  "error": {
    "code": "GAME_EXPIRED",
    "message": "This game has expired."
  }
}
```

Android should map known errors to user-friendly messages.

---

## 39. Performance Targets

Initial targets:

- Home screen usable within ~2 seconds on a normal connection.
- Recipe API p95 under ~500 ms under normal load.
- Recipe list uses pagination.
- Images use appropriate resizing/compression.
- Avoid blocking the Android main thread.
- WebSocket game events should appear near-real-time.

Measure rather than assume performance.

---

## 40. Final Cursor Prompt

After placing this `plan.md` in the repository, give Cursor the following instruction:

> Read `plan.md` completely before writing code.
>
> Build the application described in the plan as a production-ready Android + FastAPI + PostgreSQL project.
>
> Work phase-by-phase, starting with Phase 1. Do not skip architecture, migrations, testing, error handling, security, or deployment configuration.
>
> After each phase:
>
> 1. Run relevant tests.
> 2. Fix compilation/runtime errors.
> 3. Update documentation.
> 4. Clearly report what was implemented.
> 5. Do not proceed to the next phase until the current phase builds successfully.
>
> Do not use placeholder implementations for core functionality such as multiplayer voting, nutrition calculations, recipe filtering, database persistence, or API communication.
>
> Keep the code modular and production maintainable.
>
> At the end, provide exact commands for:
>
> - local Android development
> - local backend development
> - Docker startup
> - database migration
> - test execution
> - production backend deployment
> - production Android release build
> - generation of the signed `.aab`
> - Google Play submission checklist
>
> The final result must be deployable, not merely a UI prototype.
