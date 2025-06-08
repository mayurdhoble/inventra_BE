# Database Initialization Scripts

This directory contains database initialization scripts for different database systems.

## Directory Structure
- `mysql/` - MySQL-specific scripts
- `postgres/` - PostgreSQL-specific scripts

## Usage

### MySQL
```bash
mysql -u root -p inventra_db < mysql/init-data.sql
```

### PostgreSQL
```bash
psql -U postgres -d inventra_db -f postgres/init-data.sql
```

## Notes
- Scripts are idempotent (safe to run multiple times)
- Each script initializes:
  - Roles (ORG_ADMIN, BRANCH_ADMIN)
  - Address Categories (ORG_ADMIN, BRANCH_ADMIN, CONTRACTOR, SUPERVISOR)
- Make sure the database and tables exist before running the scripts
- The scripts use `WHERE NOT EXISTS` to prevent duplicate entries 