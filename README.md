# Quota

![Quota Logo](https://github.com/tovster/Quota/blob/master/src/images/logos/Quota-Logo_Black.png)

An easy to use personal finance/budgeting application built on Java 8 and JavaFX.

## Features

- Budgeting
  - Starting a budget (Easy to use wizard)
  - Account Importing
  - Unified transaction view
  - Custom categories
- Tracking pre-approved payments/bills
  - Bill Reminders
- Savings Goals
- Alerts on Overspending and Fees
- Debt Repayment Assistant
- Cloud Sync support via MySQL or MariaDB

## Usage

### Installation

- Generate the required SQL tables and include the required connection data in the Quota global class (table format to be uploaded)
- Compile using Maven and the include pom.xml
- Make a distributable from there
- (A distributable will not be released overall for the project due to the logistics of hosting an maintaining a MariaDB database)

## License

Please see [LICENSE.md](./LICENSE.md) for more information. We follow the MIT licence.
