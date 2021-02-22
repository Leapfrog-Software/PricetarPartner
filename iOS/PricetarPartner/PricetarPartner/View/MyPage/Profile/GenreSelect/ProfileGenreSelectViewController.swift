//
//  ProfileGenreSelectViewController.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

class ProfileGenreSelectViewController: UIViewController {

    private var genres = ["メディア商品", "ゲーム機・ゲームソフト", "家電・カメラ・AV機器", "パソコン・オフィス商品", "ホーム・キッチン", "食品・飲料・お酒", "ドラッグストア・ビューティ", "ベビー・おもちゃ・ホビー", "服・シューズ・バッグ・腕時計", "スポーツ＆アウトドア", "車＆バイク・産業・研究開発"]
    private var selectedIndexes = [Int]()
    private var completion: (([String]) -> ())?

    func set(defaultGenres: [String], completion: (([String]) -> ())?) {
        
        self.selectedIndexes = []
        defaultGenres.forEach { genre in
            if let index = self.genres.firstIndex(of: genre) {
                self.selectedIndexes.append(index)
            }
        }
        
        self.completion = completion
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        self.completion?(self.selectedIndexes.map { self.genres[$0] })        
        self.pop(animationType: .horizontal)
    }
}

extension ProfileGenreSelectViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.genres.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ProfileGenreSelectTableViewCell", for: indexPath) as! ProfileGenreSelectTableViewCell
        cell.configure(isSelected: self.selectedIndexes.contains(indexPath.row), genre: self.genres[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if let removeIndex = self.selectedIndexes.firstIndex(of: indexPath.row) {
            self.selectedIndexes.remove(at: removeIndex)
        } else {
            self.selectedIndexes.append(indexPath.row)
        }
        tableView.reloadData()
    }
}
