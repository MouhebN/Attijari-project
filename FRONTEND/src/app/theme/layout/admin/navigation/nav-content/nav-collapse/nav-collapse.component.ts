// Angular import
import { Component, Input, OnInit, inject } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { RouterModule } from '@angular/router';

// project import
import { NavigationItem } from '../../navigation';

import { NavItemComponent } from '../nav-item/nav-item.component';

@Component({
  selector: 'app-nav-collapse',
  standalone: true,
  imports: [CommonModule, RouterModule, NavItemComponent],
  templateUrl: './nav-collapse.component.html',
  styleUrl: './nav-collapse.component.scss'
})
export class NavCollapseComponent implements OnInit {
  private location = inject(Location);

  // public props
  @Input() item!: NavigationItem;
  windowWidth = window.innerWidth;
  current_url = ''; // Add current URL property

  ngOnInit() {
    this.current_url = this.location.path();

    // eslint-disable-next-line
    //@ts-ignore
    const baseHref = this.location['_baseHref'] || ''; // Use baseHref if necessary
    this.current_url = baseHref + this.current_url;

    // Timeout to allow DOM to fully render before checking for the links
    setTimeout(() => {
      const links = document.querySelectorAll('a.nav-link') as NodeListOf<HTMLAnchorElement>;
      links.forEach((link: HTMLAnchorElement) => {
        if (link.getAttribute('href') === this.current_url) {
          let parent = link.parentElement;
          while (parent && parent.classList) {
            if (parent.classList.contains('coded-hasmenu')) {
              parent.classList.add('coded-trigger');
              parent.classList.add('active');
            }
            parent = parent.parentElement;
          }
        }
      });
    }, 0);
  }

  // Method to handle the collapse of the navigation menu
  navCollapse(e: MouseEvent) {
    let parent = e.target as HTMLElement;

    if (parent?.tagName === 'SPAN') {
      parent = parent.parentElement!;
    }

    parent = (parent as HTMLElement).parentElement!;

    // Close all other menus first
    const sections = document.querySelectorAll('.coded-hasmenu');
    for (let i = 0; i < sections.length; i++) {
      if (sections[i] !== parent) {
        sections[i].classList.remove('coded-trigger');
      }
    }

    // Then toggle the clicked menu
    parent.classList.toggle('coded-trigger');
  }
}
