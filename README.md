# JavaTest

Experimental attempt at a different style of test framework.

## Basic Principles

- Each test should return one assertion.
- Tests should be written in plain functional Java with no magic. (I may add some magic reflection at a later date if it helps breaking tests apart, but only if it is needed.)
 
## Wishlist

- Functional composition of tests etc.
- Context aware test logging.
- Matchers
- Dependant assertions
- Assertion descriptions
- Composable exception matchers, `willThrowException(ofType(X.class, withMessage("blah), withCause(ofType(Y.class), withMessageThat(containsString("Foo")))))`
- Eventually assertion, give timeout, retry time and assertion to test. 