# Data Model

## Firestore Structure

```text
households/{householdId}
  members/{memberId}
  assets/{assetId}
  recurringPlans/{planId}
  snapshots/{snapshotId}
  imports/{importId}
```

## Member

```text
id
householdId
name
displayOrder
createdAt
updatedAt
```

## Asset

```text
id
householdId
ownerId

kind            ASSET | INVESTMENT | LIABILITY
category        SAVINGS | RETIREMENT | HOUSING_SUBSCRIPTION | CASH | DEPOSIT | LOAN | ISA | STOCK | DIVIDEND | ETC
institution
name

currentAmount
principal
quantity
currency

memo
status          ACTIVE | NEEDS_REVIEW | CLOSED
displayOrder

createdAt
updatedAt
```

## RecurringPlan

```text
id
householdId
ownerId
linkedAssetId

name
category
frequency       MONTHLY | WEEKLY | YEARLY
monthlyAmount
startDate
endDate
paymentDay
baseAmount
manualCurrentAmount
autoUpdate

createdAt
updatedAt
```

## Snapshot

Snapshots store quarterly household state for trend charts.

```text
id
householdId
period          2026-Q3
capturedAt

financialAssets
deposits
liabilities
totalAssets
netWorth
investmentPrincipal
investmentCurrentAmount
investmentProfitLoss
investmentReturn
monthlySavings

createdByImportId
createdAt
```

## Calculation Rules

```text
profitLoss = currentAmount - principal
returnRate = profitLoss / principal

financialAssets =
  investments + savings + retirement + housing subscription + cash

totalAssets = financialAssets + deposits
netWorth = totalAssets - liabilities
```

