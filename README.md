# JavaTest

Experimental attempt at a different style of test framework.

## Basic Principles

- Tests should be written in plain, simple functional Java with no magic. (I may add some magic reflection at a later date if it
helps breaking tests apart, but only if it is needed.)
- Each test should return one assertion.
- JavaTest will run a stream of tests, how you create this stream is up to you. The aim is to give control and power back
 to the writer of the tests without need to learn a new syntax.

## Simple Test definition

The most basic test can be defined by:
- A name
- A `Supplier` of an `Assertion`

e.g. `Test("One add one is two", () -> that(1 + 1 == 2));`

Optionally tests can have a collection of tags applied to them: `test("Foo", () -> that(true), List.of("bar", "baz"))`