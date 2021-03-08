//
//  AppDelegate.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/21.
//

import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        return true
    }
    
    func applicationDidBecomeActive(_ application: UIApplication) {
        
        if let tabbar = UIApplication.shared.keyWindow?.rootViewController?.getTabbarViewController() {
            tabbar.startAutoRequester()
        }
        
        UIApplication.shared.applicationIconBadgeNumber = 0
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        
        if let tabbar = UIApplication.shared.keyWindow?.rootViewController?.getTabbarViewController() {
            tabbar.stopAutoRequester()
        }
    }
}

