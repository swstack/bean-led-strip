# Bean LED Strip Firmware

This repo contains the firmware that controls the LED strip connected to the Bean and talks to the Android app in `../android/`.

# API

* `get_led_power_state`

    The LED power state is determined by the current status of the PWMs.  If any of the PWM's are active the state is considered `ON` otherwise `OFF`.

* `set_red(intensity)`
* `set_green(intensity)`
* `set_blue(intensity)`