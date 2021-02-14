# contacts

Application to read in files of contacts and sort them by various field and an
api to add and retrieve contacts.


## Usage

Run Import:

    $ java -jar contacts-0.1.0-standalone.jar import [options] <input-file>

## Options

    --sort: email: Sorts descending by email and ascending by last name
            birth: Sorts asending by birth date
            lastname: Sorts descending by last name

## Examples
    java -jar contacts-0.1.0-standalone.jar import --sort resources/test_input/mavericks.csv
...


### Compiling into Jar
    lein uberjar

### Running Tests
    lein test

## License

Copyright © 2021 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
