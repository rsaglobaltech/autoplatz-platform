# Spec-driven development with Specgate

This project was generated ready for
[Specgate](https://rsaglobaltech.github.io/specgate/): requirements with
scenarios, tests that name them, and a gate that will not call anything done
while it is red.

Lost at any point? Run `npx specgate` on its own: it shows where the
specification stands, and the one command to run next.

## Once

forja ran these for you when it installed the dependencies. If it could not:

```bash
npx specgate adopt --no-capabilities
npx specgate config set test_cmd "npm test"
```

## Where the work comes from

| You have                                           | Start with                                                             |
| -------------------------------------------------- | ---------------------------------------------------------------------- |
| A product to build, described by the client        | **A brief**: `docs/brief.md`, then `npx specgate propose --from-brief` |
| Stories and bugs in Jira, with acceptance criteria | **The tickets**: `npx specgate propose --from-ticket <KEY>`            |
| One behaviour to change                            | **A change**: `npx specgate propose <change-id>`                       |

Every way in ends in the same lifecycle: **propose → apply → archive**.

### From a brief

1. **Write the brief.** Paste the client's text into `docs/brief.md` as it
   is, fill in the four fields at its top, and give each module a section
   with its rules, examples and questions.
   `npx specgate propose --from-brief --agent "claude -p"` lets your agent
   propose them; `npx specgate propose --from-brief --check` checks the brief
   before anything is drafted.
2. **Draft the module.** `npx specgate propose --from-brief <module> --agent "claude -p"`:
   your agent writes the requirements and scenarios, and Specgate checks them
   against the brief — a value the brief never gave, a missing scenario, a
   question nobody asked. Repeat it until it says _Ready to review_.

### From a Jira ticket

1. **Connect Jira** in `alm.config.yaml`: the site, the project, the
   _names_ of the environment variables that hold the token and the email
   (never the token itself), `cloud_id: auto` for a token created with
   scopes, and which capability each epic's stories belong to (`epics:`).
2. **Is the ticket ready?** `npx specgate propose --from-ticket KAN-13 --check`
   reads it, writes nothing, and lists what the analyst should fix in Jira.
3. **Open its change.** `npx specgate propose --from-ticket KAN-13 --agent "claude -p"`
   creates the branch `feature/KAN-13-…`, copies the ticket into the change,
   and has your agent draft it against the ticket's acceptance criteria.
   Repeat it until it says _Ready to review_.

The whole path: [From a Jira ticket](https://rsaglobaltech.github.io/specgate/from-a-ticket/).

### Then, every change

1. **Review it** with whoever knows the business; the questions go to the
   client or the analyst.
2. **Build each requirement, test first.** A test that names `REQ-007` is the
   link — in Java, put it in the test's `@DisplayName`.
   `forja add usecase <module>/<Name> --req REQ-007` writes the use case and
   that test for you (without `--req`, it creates a new requirement).
   Then `npx specgate apply REQ-007`: it refuses while the gate is red.
   From a ticket, start every commit message with its key
   (`KAN-13 REQ-007: …`), so Jira shows it in the ticket.
3. **Before every pull request:** `npx specgate check`. From a ticket,
   `npx specgate alm pr <change-id>` pushes the branch and opens its one pull
   request, with the requirements and the gate in its description.
4. **Archive it** once every requirement is applied:
   `npx specgate archive <change-id>`. The requirements join the specification.

With the `ci` extra, the generated CI validates the specification on every
push and pull request.

## Where it meets the architecture

Each module of the specification is a hexagonal module of the code
(`apps/api/.../<module>/`):
its scenarios are proven by the tests of its use cases and its API.
