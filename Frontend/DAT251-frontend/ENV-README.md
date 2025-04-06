# Environment Configuration

This project uses the following environment files:

- `.env` - Default development settings (committed to git)
- `.env.local` - Local development overrides (not committed to git)
- `.env.production` - Production settings (committed to git)

## Environment Variables

- `VITE_API_BASE_URL` - URL of our own Spring Boot backend API, in the ../../backend directory

## Local Development

For local development, either use the defaults in `.env` or create an `.env.local` file with your own settings.

## Cypress ENV configuration
Now in the Cypress configuration file, it uses the node package dotenv to load the right url for the webserver based on the .env files. 
