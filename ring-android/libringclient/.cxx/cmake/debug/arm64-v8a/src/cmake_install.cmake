# Install script for directory: C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/src/main/cpp/src

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "C:/Program Files (x86)/ring")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Debug")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Install shared libraries without execute permission?
if(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)
  set(CMAKE_INSTALL_SO_NO_EXE "0")
endif()

# Is this installation the result of a crosscompile?
if(NOT DEFINED CMAKE_CROSSCOMPILING)
  set(CMAKE_CROSSCOMPILING "TRUE")
endif()

if(NOT CMAKE_INSTALL_LOCAL_ONLY)
  # Include the install script for each subdirectory.
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/client/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/config/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/dring/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/hooks/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/im/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/jamidht/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/media/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/security/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/sip/cmake_install.cmake")
  include("C:/Users/angai/AndroidStudioProjects/MyJami/ring-android/libringclient/.cxx/cmake/debug/arm64-v8a/src/upnp/cmake_install.cmake")

endif()

