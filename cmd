#!/bin/bash

lein cucumber --plugin pretty --glue test/features/step_definitions/ test/features/

