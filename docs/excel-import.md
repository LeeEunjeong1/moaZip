# Excel Import

## Goal

moaZip should support quarterly asset updates through XLSX import.

The user can keep a spreadsheet, upload it from Android, preview changes, and
apply updates to Firestore. Every successful import can also create a quarterly
snapshot.

## Supported Format

MVP supports `.xlsx` only.

## Recommended Columns

```text
assetId
owner
kind
category
institution
name
currentAmount
principal
quantity
memo
status
```

## Import Flow

1. User downloads the moaZip template.
2. User edits asset rows in Excel.
3. Android uploads the XLSX to Firebase Storage.
4. Cloud Functions parses the file.
5. App shows an import preview.
6. User chooses how to apply results:
   - add new assets only
   - update existing assets only
   - add new assets and update existing assets
7. Firestore assets are updated.
8. A quarterly snapshot is created.

## Matching Rules

- If `assetId` exists, update the matching asset.
- If `assetId` is blank, create a new asset.
- If the row is ambiguous, mark it as an import warning.

## Import Preview

Show:

- Total rows
- New assets
- Updated assets
- Rows needing review
- Expected net worth after import
- Change versus previous quarter

## Error Cases

- Unknown owner
- Unknown category
- Invalid amount
- Duplicate asset name without assetId
- Missing required name

