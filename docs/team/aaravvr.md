# Aarav Rajesh - Project Portfolio Page

## Overview

**InternTrackr** is a CLI-first internship application manager for university students applying to multiple internships.
It helps users track where they applied, current statuses, and important dates in one place so they do not miss
deadlines or lose track across spreadsheets, notes, and emails.

## Summary of Contributions

### 1. New Features

* **Adding applications (`add` command)**: Implemented the command to add a new internship application by specifying
  company and role. Includes prefix-flexible parsing (`c/` and `r/` can appear in either order), duplicate detection
  via `ApplicationList#hasApplication()` to prevent users from tracking the same application twice, and immediate
  persistence via `Storage#save()`.

* **Deleting applications (`delete` command)**: Implemented the command to remove an active internship application by
  display index. The index is resolved against the active (non-archived) entries only via
  `ApplicationList#getActiveApplication()`, then a linear scan by object identity locates the backing-list position
  for safe removal. This ensures the index always matches what the user sees in the default `list` output even when
  archived entries exist in the backing list.

* **Archiving applications (`archive` command)**: Implemented the command to hide an application from the default
  list view without deleting it. The application's `isArchived` field is set to `true` and persisted via
  `Storage#save()`. Archived applications no longer appear in `list` but remain accessible via `list archive`,
  preserving historical records without cluttering the active view.

* **Viewing archived applications (`list archive` command)**: Implemented the command to display all archived
  applications with a sequential display index. The command performs a two-pass iteration — first to count archived
  entries and determine whether to show the header, then to display them — producing clean output with no orphaned
  headers.

* **Restoring archived applications (`unarchive` command)**: Implemented the command to restore an archived
  application back to the active list. The index is resolved against archived entries only via
  `ApplicationList#getArchivedApplication()`, matching the index the user sees in `list archive`. The restored
  application immediately reappears in the default `list` output.

* **Parser redesign (split into `XYZCommandParser` classes)**: Refactored the original monolithic `Parser.java`
  (~100 lines of inline parsing logic) into a centralized dispatcher backed by dedicated per-command parser classes
  (`AddCommandParser`, `DeleteCommandParser`, `ArchiveCommandParser`, `UnarchiveCommandParser`, etc.). This keeps
  `Parser.java` as a thin switch-based router and makes each parser independently testable. Also added pipe
  character (`|`) rejection to protect the storage format.

### 2. Code Contributed

* [Link to RepoSense Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=aaravvr&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=)

### 3. Enhancements and Bug Fixes

* **Active-index-aware delete and archive**: Extended `ApplicationList` with `getActiveApplication(int displayIndex)`
  and `countActive()` to support index resolution against non-archived entries only. This was necessary to keep
  `delete` and `archive` consistent with the display indices shown in `list`.

* **Archived-index-aware unarchive**: Extended `ApplicationList` with `getArchivedApplication(int displayIndex)` and
  `countArchived()` to support index resolution against archived entries only, keeping `unarchive` consistent with
  the display indices shown in `list archive`.

* **Storage format for `isArchived`**: Designed a backward-compatible storage encoding for the archived state.
  Rather than appending a plain `true`/`false` (which conflicts with a completed deadline's `isDone` field), the
  field is encoded as `| archived:true` and only written when the application is archived. Non-archived applications
  produce identical output to before, preserving compatibility with existing data files.

* **`ListCommand` filter**: Updated `ListCommand` to skip archived applications, so the default `list` output only
  shows active entries. The empty-list message is shown only when there are no active entries.

* **Defensive coding**: Added assertions and input validation to `AddCommand`, `DeleteCommand`, `ArchiveCommand`,
  `UnarchiveCommand`, and `ListArchiveCommand` meeting the Week 9 code defensiveness requirements.

---

## Contributions to the User Guide (Extracts)

> #### **Adding an application: `add`**
> Adds an internship application with the given company and role. Prefixes can be in any order. Duplicate applications are not allowed.
> **Format:** `add c/COMPANY r/ROLE`
> * **Example:** `add c/"Shopee" r/"Backend Intern"`
>
> #### **Deleting an application: `delete`**
> Deletes the specified active application by its display index. Archived applications are excluded from the index.
> **Format:** `delete INDEX`
> * **Example:** `delete 2`
>
> #### **Archiving an application: `archive`**
> Archives an application so it no longer appears in the default list, but is not deleted. Can be viewed with `list archive`.
> **Format:** `archive INDEX`
> * **Example:** `archive 1`
>
> #### **Restoring an archived application: `unarchive`**
> Restores an archived application back to the active list. The index refers to the index shown in `list archive`.
> **Format:** `unarchive INDEX`
> * **Example:** `unarchive 1`
>
> #### **Listing archived applications: `list archive`**
> Displays all archived applications using a sequential index starting from 1.
> **Format:** `list archive`

---

## Contributions to the Developer Guide (Extracts)

> #### **Add Feature Implementation**
> The `add` command uses `AddCommandParser` to support prefix-flexible input (`c/` and `r/` in any order). `AddCommand#execute()` checks `ApplicationList#hasApplication()` to reject duplicates before insertion, then calls `Storage#save()` to persist the new entry immediately.
>
> #### **Delete Feature Implementation**
> The `delete` command resolves the display index against active (non-archived) entries only via `ApplicationList#getActiveApplication()`. A linear scan by object identity then locates the backing-list position for safe removal, ensuring the index always matches what the user sees in `list`. The confirmation message shows the remaining active count via `ApplicationList#countActive()`.
>
> #### **Parser Implementation**
> The original `Parser.java` was refactored from a ~100-line monolithic method into a centralized dispatcher backed by dedicated `XYZCommandParser` classes (`AddCommandParser`, `DeleteCommandParser`, `ArchiveCommandParser`, `UnarchiveCommandParser`, etc.). `Parser.java` now acts as a thin switch-based router. Pipe character (`|`) input is also rejected to protect the storage format.
>
> #### **Archive Feature Implementation**
> The `archive` command resolves the display index via `ApplicationList#getActiveApplication()` and sets `Application#isArchived` to `true`. The archived state is stored using the token `| archived:true`, only appended when the application is archived, to avoid conflict with deadline `isDone` fields.
>
> #### **List Archive Feature Implementation**
> `ListArchiveCommand` performs a two-pass iteration — first to count archived entries, then to display them with a sequential index. This avoids printing an orphaned header when no archived applications exist.
>
> #### **Unarchive Feature Implementation**
> The `unarchive` command resolves the display index against archived entries only via `ApplicationList#getArchivedApplication()`, ensuring the index matches what the user sees in `list archive`. `Application#setArchived(false)` restores the application, which immediately reappears in the default `list` output. The design is intentionally symmetric with `archive`.
> 