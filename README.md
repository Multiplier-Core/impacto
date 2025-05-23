# Impacto

Impacto is an AI companion tool to analyze code change impacts with features including CLI triggering, plugin-based discovery, domain enrichment, multi-repo integration, and confidence scoring.

## AI Service Integration

Impacto integrates with the AI service to fetch component and feature information. This integration is implemented in the following files:

- `src/main/kotlin/com/multiplier/impacto/client/AiServiceClient.kt`: Client for interacting with the AI service
- `src/main/kotlin/com/multiplier/impacto/rest/ComponentController.kt`: REST controller for component-related operations
- `src/main/kotlin/com/multiplier/impacto/config/RestClientConfig.kt`: Configuration for REST clients

### Using the AI Service Client

The `AiServiceClient` provides methods to fetch component and feature information from the AI service:

```kotlin
// Inject the AiServiceClient
@Autowired
private lateinit var aiServiceClient: AiServiceClient

// Get all components
val components = aiServiceClient.getAllComponents()

// Get all component names
val componentNames = aiServiceClient.getAllComponentNames()

// Get a component by ID
val component = aiServiceClient.getComponentById("auth")

// Get all features
val features = aiServiceClient.getAllFeatures()

// Get all feature names
val featureNames = aiServiceClient.getAllFeatureNames()

// Get a feature by ID
val feature = aiServiceClient.getFeatureById("user-auth")
```

### REST API Endpoints

The `ComponentController` provides REST API endpoints for component-related operations:

- `GET /api/components`: Get all components
- `GET /api/components/names`: Get all component names
- `GET /api/components/{id}`: Get a component by ID
- `GET /api/components/features`: Get all features
- `GET /api/components/features/names`: Get all feature names
- `GET /api/components/features/{id}`: Get a feature by ID

## Configuration

The AI service URL can be configured in the `application.yml` file:

```yaml
ai:
  service:
    url: http://localhost:8122
```

## Running the Application

To run the application, use the following command:

```bash
./gradlew bootRun
```

## Testing

To run the tests, use the following command:

```bash
./gradlew test
```
