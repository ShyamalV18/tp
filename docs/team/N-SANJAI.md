# Navaneethan Sanjai - Project Portfolio Page

## Overview
**InternTrackr** is a CLI-first internship application manager for university students applying to multiple internships. It helps users track where they applied, current statuses, and important dates in one place so they do not miss deadlines or lose track across spreadsheets, notes, and emails.

## Summary of Contributions

### 1. New Features
* **Recruiter Networking (`contact` command)**: Developed the functionality to link recruiter names and emails directly to specific applications. This localizes networking details, ensuring users have immediate access to follow-up contacts without switching platforms or searching through emails.
* **Status Analytics (`overview` command)**: Enhanced the overview feature to compute and display a quantitative breakdown of tracked applications by their current statuses (e.g., Applied, Pending, Interview). This gives users an immediate sense of their job hunt momentum.
* **Compensation Tracking (`offer` command)**: Implemented the logic to log salary details for successful applications. This command automatically updates the application's status to "Offered" and securely persists the financial data, allowing users to compare compensation packages.
* **In-App Assistance (`help` command)**: Developed a lightweight help command that directly points users to the comprehensive online documentation, ensuring a smooth onboarding experience.

### 2. Code Contributed
* [Link to RepoSense Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=n-sanjai&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

### 3. Contributions to the User Guide (Extracts)

> #### **Adding a contact: `contact`**
> Links recruiter or networking details to a specific internship application.
> **Format:** `contact INDEX c/NAME e/EMAIL`
> * **Example:** `contact 1 c/"Jane Smith HR" e/"jane.smith@shopee.com"`
>
> #### **Logging an offer: `offer`**
> Updates an application with an offered salary and automatically changes its status to "Offered".
> **Format:** `offer INDEX SALARY`
> * **Example:** `offer 1 5000.00`
>
> #### **Viewing overview: `overview`**
> Shows a quick summary of your internship applications.
> **Format:** `overview`
> Overview includes:
> * Total number of applications
> * Count of applications broken down by current status
>
> #### **Viewing help: `help`**
> Shows a message explaining how to access the help page.
> **Format:** `help`

### 4. Contributions to the Developer Guide (Extracts)

> #### **Contact Feature Implementation**
> The `contact` command allows users to store recruiter details directly alongside a specific application. `ContactCommand#execute()` verifies the index bounds, updates the internal state of the selected `Application` with the provided name and email, and immediately triggers `Storage#save()` to ensure these networking details are securely written to disk.
>
> #### **Overview Feature Implementation**
> The `OverviewCommand` queries `ApplicationList` to calculate current application counts and iterates through the list to aggregate status frequencies. It passes this mapped data to the `Ui` to format and display the summary. Since it is a read-only operation, it safely avoids invoking `Storage`.
>
> #### **Offer Feature Implementation**
> The `offer` command is handled by `OfferCommand`. It follows a strict execution pipeline: validates the index bounds, updates the target `Application` object's salary, and checks the previous status. If the status was not already "Offered," it auto-updates it. Finally, it immediately triggers `Storage#save()` to ensure financial data is persistently stored without requiring manual user action.
>
> #### **Help Feature Implementation**
> The `help` feature is an isolated command that utilizes the `Ui` to project the application's external User Guide URL to the terminal, allowing users to find comprehensive parameter instructions easily without bloating the CLI view.
> 