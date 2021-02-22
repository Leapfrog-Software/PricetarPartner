//
//  UIImage+Extension.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

extension UIImage {
        
    func toUserImage() -> UIImage? {

        if self.size.width > self.size.height {
            let width = 800 / UIScreen.main.scale
            let height = width * self.size.height / self.size.width
            return self.resize(size: CGSize(width: width, height: height))
        } else {
            let height = 800 / UIScreen.main.scale
            let width = height * self.size.width / self.size.height
            return self.resize(size: CGSize(width: width, height: height))
        }
    }
    
    func resize(size: CGSize) -> UIImage? {
        
        UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
        draw(in: CGRect(origin: .zero, size: size))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return resizedImage
    }
    
    func trim(rect: CGRect) -> UIImage? {
        
        UIGraphicsBeginImageContextWithOptions(rect.size, false, 0)
        draw(at: CGPoint(x: -rect.origin.x, y: -rect.origin.y))
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return image
    }
}
