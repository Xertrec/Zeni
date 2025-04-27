# Zeni: The Travel Planner app

## Team
- Alex

## Project Description
Zeni is a travel planning application designed to help users organize their trips efficiently. The app provides features such as itinerary management, trip planning, and user preferences. It aims to offer a seamless experience for travelers by integrating various functionalities into a single platform.

## Features
- [x] Itinerary creation and management
- [x] User settings and preferences to customize the aspect of the app.
- [x] Multi-language support for a better user experience.
- [ ] Map selection of destinations.
- [ ] Weather forecast for the selected destination.
- [ ] AI recommendations for activities and places to visit.
- [ ] Notifications for upcoming trips and activities.

## Getting Started
To get started with the project, clone the repository and open it in Android Studio. Make sure you have the required Android and Kotlin versions installed.

## Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/your-repo/zeni-travel-planner.git
    ```
2. Open the project in Android Studio.
3. Build and run the project on an emulator or physical device.

## Contributing
Please read the `CONTRIBUTING.md` file for details on our code of conduct, and the process for submitting pull requests.

## Directory structure
      .
      ├── .github                               # CI files.
      ├── app/src
      │   ├── androidTest                       # Tests for android only.
      │   ├── main/java/com/zeni
      │   │   ├── auth                          # Authentication related functions and UI.
      │   │   ├── core                          # Share code.
      │   │   │   ├── data                      # Model related classes.
      │   │   │   ├── di                        # Dependency injection related classes.
      │   │   │   ├── domain                    # Business logic related classes.
      │   │   │   └── presentation              # UI related classes.
      │   │   ├── home                          # Customizable screen with shortcuts.
      │   │   ├── itinerary                     # Itinerary related functions and UI.
      │   │   ├── settings                      # Settings related functions and UI.
      │   │   └── trip                          # Trip related functions and UI.
      │   └── test                              # Tests
      ├── docs                                  # Documentation files.
      └── zLogos                                # Zeni logos.

## License
This project is licensed under the Apache 2.0 License - see the `LICENSE` file for details.

## Acknowledgments
- Thanks to the team members for their contributions.
- Special thanks to the open-source community for providing valuable resources and tools.
- And to Alberto, for being one of the members who conceived the idea.