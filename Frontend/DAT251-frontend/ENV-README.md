# Environment Configuration

This project uses the following environment files:

- `.env` - Default development settings (committed to git)
- `.env.local` - Local development overrides (not committed to git)
- `.env.production` - Production settings (committed to git)

## Environment Variables

- `VITE_SPRING_API_URL` - URL of the Spring Boot backend API

## Local Development

For local development, either use the defaults in `.env` or create an `.env.local` file with your own settings.

## CI/CD Pipeline

The CI/CD pipeline creates its own environment settings and doesn't use the committed `.env` files.
