# moaZip

moaZip is a private household asset dashboard app for couples and families.

It is not a daily expense tracker. The first goal is to update assets quarterly,
see how much the household net worth has grown, and keep savings, investments,
deposits, liabilities, and recurring savings in one clear place.

## Product Direction

- Android: primary app for input, updates, Excel import, and automatic calculations.
- Web: read-first dashboard for PC and Mac, with charts and asset list views.
- Backend: Firebase Auth, Firestore, Firebase Storage, and Cloud Functions.
- MVP excludes bank/card auto-linking and MyData APIs.

## Monorepo Layout

```text
android/   Android app, planned with Kotlin and Jetpack Compose
web/       Web dashboard, planned with React or Next.js
docs/      Product planning, data model, and import specs
```

## Core MVP

- Household dashboard
- Asset and investment list
- Quarterly asset updates
- Net worth growth by quarter
- Investment profit/loss and return calculation
- Recurring savings plans
- XLSX import and export
- Firestore sync

## Calculation Rules

- Profit/loss = current amount - principal
- Return = profit/loss / principal
- Financial assets = investments + savings + retirement + housing subscription + cash
- Total assets = financial assets + deposits
- Net worth = total assets - liabilities

