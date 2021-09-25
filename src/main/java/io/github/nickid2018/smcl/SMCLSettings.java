/*
 * Copyright 2021 Nickid2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.nickid2018.smcl;

/**
 * The settings of the SMCL System
 */
public class SMCLSettings {

    // Optimizing settings
    /**
     * Disable number pool in parsing statement if true
     */
    public final boolean disableNumberPool = false;
    // Calculating settings
    /**
     * Using degree function to compute trigonometric functions if true
     */
    public boolean degreeAngle = false;
    /**
     * Ignore errors in computing and print the information if true
     */
    public boolean invalidArgumentWarn = false;
}
