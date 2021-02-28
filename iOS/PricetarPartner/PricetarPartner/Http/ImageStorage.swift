//
//  ImageStorage.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/26.
//

import UIKit

class ImageStorage {
    
    private static let expirationInterval = TimeInterval(5 * 60)    // 5分

    private struct RequestData {
        let url: String
        let imageView: UIImageView
        let errorImage: UIImage?
    }
    
    private struct CacheData {
        let url: String
        let image: UIImage
        let datetime: Date
    }

    static let shared = ImageStorage()
    
    private var requests = [RequestData]()
    private var caches = [CacheData]()
    
    func fetch(url: String, imageView: UIImageView, errorImage: UIImage? = nil, completion: ((Bool) -> ())? = nil) {
        
        self.cancelRequest(imageView: imageView)
        
        if let image = self.caches.first(where: { $0.url == url && Date().timeIntervalSince($0.datetime) <= ImageStorage.expirationInterval })?.image {
            imageView.image = image
            completion?(true)
            return
        }
        
        self.requests.append(RequestData(url: url, imageView: imageView, errorImage: errorImage))
        
        self.readLocalFile(url: url, completion: { image in
            if let image = image {
                self.applyResult(url: url, imageView: imageView, image: image, completion: completion)
            } else {
                self.fetchRemoteFile(url: url, completion: { image in
                    self.applyResult(url: url, imageView: imageView, image: image, completion: completion)
                })
            }
        })
    }
    
    private func applyResult(url: String, imageView: UIImageView, image: UIImage?, completion: ((Bool) -> ())? = nil) {
        
        DispatchQueue.main.async {
            self.requests.filter { $0.url == url }.forEach { requestData in
                requestData.imageView.image = image
            }
            self.cancelRequest(imageView: imageView)
            
            completion?(image != nil)
            
            if let image = image {
                self.caches.removeAll(where: { Date().timeIntervalSince($0.datetime) > ImageStorage.expirationInterval })
                if self.caches.count > 50 {
                    self.caches.remove(at: 0)
                }
                self.caches.append(CacheData(url: url, image: image, datetime: Date()))
            }
        }
    }
    
    private func getRootDirectory() -> URL? {
        return FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first
    }
    
    private func createLocalPath(url: String) -> URL? {
        
        if let rootUrl = self.getRootDirectory() {
            if var encodedUrl = url.base64Encode() {
                if encodedUrl.count > 64 {
                    encodedUrl = encodedUrl.substr(start: encodedUrl.count - 64, length: 64)
                }
                return rootUrl.appendingPathComponent(encodedUrl)
            }
        }
        return nil
    }
    
    private func readLocalFile(url: String, completion: @escaping ((UIImage?) -> ())) {
        
        DispatchQueue.global().async {
            guard let imagePath = self.createLocalPath(url: url) else {
                completion(nil)
                return
            }
            let manager = FileManager()
            do {
                let attrs = try manager.attributesOfItem(atPath: imagePath.path)
                if let modificationDate = attrs[.modificationDate] as? Date {
                    if Date().timeIntervalSince(modificationDate) > ImageStorage.expirationInterval {
                        completion(nil)
                        return
                    }
                }
            } catch {}

            let image = UIImage(contentsOfFile: imagePath.path)
            completion(image)
        }
    }
    
    private func saveLocalFile(url: String, image: UIImage, completion: @escaping ((UIImage?) -> ())) {

        guard let imageData = image.pngData(), let imagePath = self.createLocalPath(url: url) else {
            completion(nil)
            return
        }
        
        do {
            try imageData.write(to: imagePath, options: .atomic)
        } catch {
            completion(nil)
            return
        }
        completion(image)
    }
    
    private func fetchRemoteFile(url: String, completion: @escaping ((UIImage?) -> ())) {
        
        HttpRequester.get(url: url) { result, data in
            DispatchQueue.global().async {
                if result, let data = data, let image = UIImage(data: data) {
                    self.saveLocalFile(url: url, image: image, completion: completion)
                } else {
                    if let errorImage = (self.requests.filter { $0.url == url }).first?.errorImage {
                        self.saveLocalFile(url: url, image: errorImage, completion: completion)
                        completion(errorImage)
                    } else if let noImage = UIImage(named: "no_image") {
                        self.saveLocalFile(url: url, image: noImage, completion: completion)
                        completion(noImage)
                    } else {
                        completion(nil)
                    }
                }
            }
        }
    }
    
    func deleteLocalFile(url: String) -> Bool {
        
        if let imagePath = self.createLocalPath(url: url) {
            do {
                try FileManager.default.removeItem(at: imagePath)
                return true
            } catch {
                return false
            }
        }
        return false
    }
    
    func cancelRequest(imageView: UIImageView) {
        self.requests.removeAll(where: { $0.imageView == imageView })
    }
    
    func query(url: String, completion: @escaping ((UIImage?) -> ())) {
        
        guard let imagePath = self.createLocalPath(url: url) else {
            completion(nil)
            return
        }
        
        DispatchQueue.global().async {
            let image = UIImage(contentsOfFile: imagePath.path)
            DispatchQueue.main.async {
                completion(image)
            }
        }
    }
    
    func removeAll() {

        guard var rootUrl = self.getRootDirectory()?.absoluteString else {
            return
        }
        if rootUrl.substr(start: 0, length: 7) == "file://" {
            rootUrl = rootUrl.substr(start: 7, length: rootUrl.count - 7)
        }
        if let contents = try? FileManager.default.contentsOfDirectory(atPath: rootUrl) {
            contents.forEach { content in
                let filePath = rootUrl + "/" + content
                try? FileManager.default.removeItem(atPath: filePath)
            }
        }
        self.caches.removeAll()
    }
}
