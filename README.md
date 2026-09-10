# Sacoche-WS

REST web service exporting the ENT directory to Sacoche, replacing the previous manual CSV export.

## Endpoints

| Method | Route | Description | Auth |
|---|---|---|---|
| `GET` | `/health-check` | Checks that the application is up | Public |
| `GET` | `/api/export/{uai}` | Exports the people (teachers, staff, students, parents) linked to Sacoche for the given establishment | Required |

The export queries 4 LDAP populations separately (`ENTAuxEnseignant`, `ENTAuxNonEnsEtab`, `ENTEleve`, `ENTAuxPersRelEleve`), keeps only entries tagged `ESCOPersonExternalIds: SACOCHE$<id>`, and deduplicates them by `uid`.

## Security

Every call to `/api/**` must provide:
- an `x-api-key` header matching the configured key (`app.security.api-key`)
- a source IP address on the allowlist (`app.security.authorized-ip-access`, in addition to `127.0.0.1`/`::1`, always accepted)

## Configuration

All sensitive/environment-specific values (`application.yaml`) are left empty and injected via environment variables — never committed. See the `app.ldap.*` and `app.security.*` prefixes in `src/main/resources/application.yaml` for the full list of expected keys.

## Running locally

1. Start a test LDAP server (e.g. `docker-openldap-test`):
   ```bash
   ./scripts/up.sh
   ```
   On first run, it will ask for the path to the schema reference repository (`SCHEMA_REFERENCE_DIR`) unless already set via `.env`.
2. Set the `APP_LDAP_*`/`APP_SECURITY_*` environment variables in the run configuration (IntelliJ: check **"Include dependencies with 'Provided' scope"**, otherwise startup fails with an error related to `jakarta.servlet.Filter`)
3. Run the application, then test it:
   ```bash
   curl -i -H "x-api-key: <your-key>" "http://localhost:8080/api/export/<uai>"
   ```

## Tests

```bash
./mvnw test
```

A GitHub Actions workflow (`.github/workflows/test.yml`) runs the suite on every push/pull request. It can be replayed locally with [`act`](https://github.com/nektos/act):
```bash
act push
```

## Notice and license commands

- `./mvnw notice:check`
- `./mvnw notice:generate`
- `./mvnw license:check`
- `./mvnw license:format`
- `./mvnw license:remove`
