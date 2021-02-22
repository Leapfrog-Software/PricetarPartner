//
//  ProfileGenreSelectTableViewCell.swift
//  PricetarPartner
//
//  Created by Leapfrog-Inc. on 2021/02/22.
//

import UIKit

class ProfileGenreSelectTableViewCell: UITableViewCell {

    @IBOutlet private weak var selectedImageView: UIImageView!
    @IBOutlet private weak var genreLabel: UILabel!
    
    func configure(isSelected: Bool, genre: String) {
        
        self.selectedImageView.isHidden = !isSelected
        self.genreLabel.text = genre
    }
}
