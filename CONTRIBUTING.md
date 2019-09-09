# Contributing to Apiator

Hello! And thank you for you time contributing to Apiator!

This doc contains guidelines and best practices accepted on the project.
They are not rules so feel free to propose changes to this list and use your best judgment for anything that isn't described.

## Q and A

This part it built like 'Question and Answer' session to cover most popular issues

### **You've found a bug! (You have a great idea!)**

* First of all ensure that similar [issue](https://github.com/ainrif/apiator/issues) isn't already reported.

* If you're unable to find an open issue addressing the problem, [open a new one](https://github.com/ainrif/apiator/issues/new/choose).
  Select the type and follow the template.
  Be sure to provide a _short and clear title_.
  It will help other contributors to find this issue easier.

* If you have a code sample in you fork you can create a _pull request_.
  Please **do not** create a PR **without** the issue.
  We track our backlog and tasks only via the issues.

### **You have a patch in your fork**

* First of all create an issue, please see the [first question](https://github.com/ainrif/apiator/blob/feature/update_readme/CONTRIBUTING.md#youve-found-a-bug-you-have-great-idea).
  Then describe there an essence of the _pull request_.

* Then create a pull request and attach it to the issue (f.e. add PR number to the issue description)

### **I want to change (extend) documentation**

* Documentation is treated as a code.
  Therefore there is no difference between PR for the doc or for the main source code.

* To show that you have doc related change please select the corresponding issue type.

## Styleguides

### **Git Commit Messages**

* Use the present tense - "add feature" (not "added feature") to be short.
* Use the imperative mood - "move component to..." (not "moves component to...") to express intent.
* Use small first letter and avoid dot in the end - "change title" (not "Change title.") to make first line in one sentence.
* Limit the first line about 72 characters.
* Use comment description to leave additional info - `git commit -am "add feature" -m "PR #42"` to keep history clear.

### **Groovy Style Guide**

* Based on [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html#s4.2-block-indentation) with overrides:
    * [4.2](https://google.github.io/styleguide/javaguide.html#s4.2-block-indentation) Block indentation: +4 spaces
    * [4.4](https://google.github.io/styleguide/javaguide.html#s4.4-column-limit) Column limit: 120
    * [5.2.1](https://google.github.io/styleguide/javaguide.html#s5.2.1-package-names) Package names: one underscore in the middle of package name is acceptable

* Then apply [Groovy Style Guide](http://docs.groovy-lang.org/docs/next/html/documentation/style-guide.html)

* If you have [IntelliJ IDEA](https://www.jetbrains.com/idea/) then default embedded formatter should be pretty good

### **JavaScript Style Guide**

* TBD

## Issues workflow

The following describes how issue goes through the stages from 'new' to 'done'

* Newly created issues goes to [filter](https://github.com/ainrif/apiator/issues?utf8=%E2%9C%93&q=is%3Aissue+is%3Aopen+no%3Aproject)
    and get the label '&#x1F4DC; brand new'.

* If issue is considered as something that should be done it loses the tag '&#x1F4DC; brand new' and goes to the backlog.
  Otherwise issue will be closed.

* From the backlog issue goes to the ['To do' queue](https://github.com/ainrif/apiator/projects/1) (via assigning the project).

* When the issue is scheduled to the release it gets its milestone and waits for implementation.

* During the implementation issue is situated in ['In progress' queue](https://github.com/ainrif/apiator/projects/1).

* After implementation the issue must be tested or reviewed in ['Review/Testing in progress' queue](https://github.com/ainrif/apiator/projects/).
  And as a result it can go to the 'In progress' queue again or to the 'Approved/Tested'.

* When the issues is 'Approved/Tested' the author merges it into the `develop` branch and closes the issue.

* After the release all issues in this milestone go to the archive.

* Ticket workflow is done.
